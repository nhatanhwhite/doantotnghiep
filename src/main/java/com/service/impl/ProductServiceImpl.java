package com.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import com.entity.Category;
import com.entity.Product;
import com.entity.ProductImage;
import com.entity.Thoroughbred;
import com.entity.UserSystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.repository.CategoryRepository;
import com.repository.ProductImageRepository;
import com.repository.ProductRepository;
import com.repository.ThoroughbredRepository;
import com.repository.UserSystemRepository;
import com.service.ProductService;
import com.service.dto.DataDTO;
import com.utils.FileStorageUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Value("${app.image.url}")
	private String folder;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ThoroughbredRepository thoroughbredRepository;

	@Autowired
	private UserSystemRepository userSystemRepository;

	@Autowired
	private ProductImageRepository productImageRepository;

	@Override
	public Product save(String product, String category, String thoroughbred, List<MultipartFile> files) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());

			// List<MultipartFile> multipartFiles = mapper.readValue(file, new
			// TypeReference<List<MultipartFile>>() {
			// });

			Product productData = mapper.readValue(product, Product.class);
			Optional<Category> categoryData = categoryRepository.findById(Long.parseLong(category));
			Optional<Thoroughbred> thoroughbredData = thoroughbredRepository.findById(Long.parseLong(thoroughbred));

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserSystem userSystem = userSystemRepository.findByEmail(authentication.getName());

			productData.setQuantitySell(0);
			productData.setInventory(productData.getQuantityImport());
			productData.setLastUpdate(LocalDate.now());
			productData.setCategory(categoryData.get());
			productData.setThoroughbred(thoroughbredData.get());
			productData.setUserSystem(userSystem);

			Product result = productRepository.save(productData);

			FileStorageUtils fileStorageUtils = new FileStorageUtils();
			Path rootPath = Paths.get(folder);
			List<ProductImage> productImages = new ArrayList<>();

			for (MultipartFile multipartFile : files) {
				String fileName = fileStorageUtils.setNameImage(multipartFile.getOriginalFilename());

				ProductImage productImage = new ProductImage();
				productImage.setProduct(result);
				productImage.setImage(fileName);

				productImages.add(productImage);
				fileStorageUtils.save(multipartFile, fileName, rootPath);
			}

			productImageRepository.saveAll(productImages);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Product> findAll() {
		List<Product> products = productRepository.findAll();

		return products;
	}

	@Override
	public List<DataDTO> findAll2() {
		List<Product> products = productRepository.findAll();
		List<ProductImage> productImages = productImageRepository.findAll();

		Map<Long, List<ProductImage>> map = new HashMap<>();

		for (ProductImage productImage : productImages) {
			Long id = productImage.getProduct().getId();

			List<ProductImage> listImages = new ArrayList<>();

			if (productImage.getProduct().getId() == id) {
				listImages.add(productImage);
			}

			map.put(productImage.getProduct().getId(), listImages);
		}

		List<DataDTO> dataDTOS = new ArrayList<>();

		for (Product product : products) {
			DataDTO dataDTO = new DataDTO();

			String image = "";

			if (map.get(product.getId()) != null && map.get(product.getId()).size() > 0) {
				image = map.get(product.getId()).get(0).getImage();
			}

			dataDTO.setId(product.getId());
			dataDTO.setProductName(product.getProductName());
			dataDTO.setIntroduce(product.getIntroduce());
			dataDTO.setDescription(product.getDescription());
			dataDTO.setPriceImport(product.getPriceImport());
			dataDTO.setPriceSell(product.getPriceSell());
			dataDTO.setQuantityImport(product.getQuantityImport());
			dataDTO.setQuantitySell(product.getQuantitySell());
			dataDTO.setInventory(product.getInventory());
			dataDTO.setSale(product.getSale());
			dataDTO.setLastUpdate(product.getLastUpdate());
			dataDTO.setImage(image);
			dataDTO.setThoroughbred(product.getThoroughbred());
			dataDTO.setCategory(product.getCategory());
			dataDTO.setUserSystem(product.getUserSystem());

			dataDTOS.add(dataDTO);
		}
		return dataDTOS;
	}

	@Override
	public Product findById(Long id) {
		Optional<Product> product = productRepository.findById(id);

		if (product.isPresent()) {
			return product.get();
		}

		return null;
	}

	@Override
	public Product update(String product, String category, String thoroughbred) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());

			Product productData = mapper.readValue(product, Product.class);
			Optional<Category> categoryData = categoryRepository.findById(Long.parseLong(category));
			Optional<Thoroughbred> thoroughbredData = thoroughbredRepository.findById(Long.parseLong(thoroughbred));

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserSystem userSystem = userSystemRepository.findByEmail(authentication.getName());

			productData.setInventory(productData.getQuantityImport() - productData.getQuantitySell());
			productData.setLastUpdate(LocalDate.now());
			productData.setCategory(categoryData.get());
			productData.setThoroughbred(thoroughbredData.get());
			productData.setUserSystem(userSystem);

			return productRepository.save(productData);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void delete(Long id) {
		Product product = this.findById(id);

		List<ProductImage> productImages = productImageRepository.findByProduct(product);

		FileStorageUtils fileStorageUtils = new FileStorageUtils();
		Path rootPath = Paths.get(folder);

		for (ProductImage productImage : productImages) {
			fileStorageUtils.delete(rootPath, productImage.getImage());
		}

		productImageRepository.deleteAll(productImages);

		productRepository.deleteById(id);
	}

	@Override
	public Product updateImage(String id, List<MultipartFile> files) {
		try {
			Product product = this.findById(Long.parseLong(id));

			List<ProductImage> productImages = productImageRepository.findByProduct(product);

			FileStorageUtils fileStorageUtils = new FileStorageUtils();
			Path rootPath = Paths.get(folder);

			for (ProductImage productImage : productImages) {
				fileStorageUtils.delete(rootPath, productImage.getImage());
			}

			productImageRepository.deleteAll(productImages);

			List<ProductImage> newImages = new ArrayList<>();

			for (MultipartFile multipartFile : files) {
				String fileName = fileStorageUtils.setNameImage(multipartFile.getOriginalFilename());

				ProductImage productImage = new ProductImage();
				productImage.setProduct(product);
				productImage.setImage(fileName);

				newImages.add(productImage);
				fileStorageUtils.save(multipartFile, fileName, rootPath);
			}

			productImageRepository.saveAll(newImages);

			return product;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<DataDTO> findTop8ProductNews() {
		List<Product> products = productRepository.findTop12BySaleEqualsOrderByLastUpdateDesc(0);
		List<ProductImage> productImages = productImageRepository.findAll();

		Map<Long, List<ProductImage>> map = new HashMap<>();

		for (ProductImage productImage : productImages) {
			Long id = productImage.getProduct().getId();

			List<ProductImage> listImages = new ArrayList<>();

			if (productImage.getProduct().getId() == id) {
				listImages.add(productImage);
			}

			map.put(productImage.getProduct().getId(), listImages);
		}

		List<DataDTO> dataDTOS = new ArrayList<>();

		for (Product product : products) {
			DataDTO dataDTO = new DataDTO();
			String image = "";

			if (map.get(product.getId()) != null && map.get(product.getId()).size() > 0) {
				image = map.get(product.getId()).get(0).getImage();
			}

			dataDTO.setId(product.getId());
			dataDTO.setProductName(product.getProductName());
			dataDTO.setIntroduce(product.getIntroduce());
			dataDTO.setDescription(product.getDescription());
			dataDTO.setPriceImport(product.getPriceImport());
			dataDTO.setPriceSell(product.getPriceSell());
			dataDTO.setQuantityImport(product.getQuantityImport());
			dataDTO.setQuantitySell(product.getQuantitySell());
			dataDTO.setInventory(product.getInventory());
			dataDTO.setSale(product.getSale());
			dataDTO.setLastUpdate(product.getLastUpdate());
			dataDTO.setImage(image);
			dataDTO.setThoroughbred(product.getThoroughbred());
			dataDTO.setCategory(product.getCategory());
			dataDTO.setUserSystem(product.getUserSystem());

			dataDTOS.add(dataDTO);
		}

		return dataDTOS;
	}

	@Override
	public List<DataDTO> findTop8ProductSale() {
		List<Product> products = productRepository.findTop8BySaleGreaterThanOrderByLastUpdateDesc(0);

		List<ProductImage> productImages = productImageRepository.findAll();

		Map<Long, List<ProductImage>> map = new HashMap<>();

		for (ProductImage productImage : productImages) {
			Long id = productImage.getProduct().getId();

			List<ProductImage> listImages = new ArrayList<>();

			if (productImage.getProduct().getId() == id) {
				listImages.add(productImage);
			}

			map.put(productImage.getProduct().getId(), listImages);
		}

		List<DataDTO> dataDTOS = new ArrayList<>();

		for (Product product : products) {

			DataDTO dataDTO = new DataDTO();

			String image = "";

			if (map.get(product.getId()) != null && map.get(product.getId()).size() > 0) {
				image = map.get(product.getId()).get(0).getImage();
			}
			dataDTO.setId(product.getId());
			dataDTO.setProductName(product.getProductName());
			dataDTO.setIntroduce(product.getIntroduce());
			dataDTO.setDescription(product.getDescription());
			dataDTO.setPriceImport(product.getPriceImport());
			dataDTO.setPriceSell(product.getPriceSell());
			dataDTO.setQuantityImport(product.getQuantityImport());
			dataDTO.setQuantitySell(product.getQuantitySell());
			dataDTO.setInventory(product.getInventory());
			dataDTO.setSale(product.getSale());
			dataDTO.setLastUpdate(product.getLastUpdate());
			dataDTO.setImage(image);
			dataDTO.setThoroughbred(product.getThoroughbred());
			dataDTO.setCategory(product.getCategory());
			dataDTO.setUserSystem(product.getUserSystem());

			dataDTOS.add(dataDTO);
		}

		return dataDTOS;
	}

	@Override
	public List<DataDTO> findByCategory(Category category) {
		List<Product> products = productRepository.findByCategoryAndSaleEqualsOrderByLastUpdateDesc(category, 0);
		List<ProductImage> productImages = productImageRepository.findAll();

		Map<Long, List<ProductImage>> map = new HashMap<>();

		for (ProductImage productImage : productImages) {
			Long id = productImage.getProduct().getId();

			List<ProductImage> listImages = new ArrayList<>();

			if (productImage.getProduct().getId() == id) {
				listImages.add(productImage);
			}

			map.put(productImage.getProduct().getId(), listImages);
		}

		List<DataDTO> dataDTOS = new ArrayList<>();

		for (Product product : products) {
			DataDTO dataDTO = new DataDTO();

			String image = map.get(product.getId()).get(0).getImage();

			dataDTO.setId(product.getId());
			dataDTO.setProductName(product.getProductName());
			dataDTO.setIntroduce(product.getIntroduce());
			dataDTO.setDescription(product.getDescription());
			dataDTO.setPriceImport(product.getPriceImport());
			dataDTO.setPriceSell(product.getPriceSell());
			dataDTO.setQuantityImport(product.getQuantityImport());
			dataDTO.setQuantitySell(product.getQuantitySell());
			dataDTO.setInventory(product.getInventory());
			dataDTO.setSale(product.getSale());
			dataDTO.setLastUpdate(product.getLastUpdate());
			dataDTO.setImage(image);
			dataDTO.setThoroughbred(product.getThoroughbred());
			dataDTO.setCategory(product.getCategory());
			dataDTO.setUserSystem(product.getUserSystem());

			dataDTOS.add(dataDTO);
		}

		return dataDTOS;
	}

	@Override
	public List<DataDTO> findByCategoryAndSale(Category category) {
		List<Product> products = productRepository.findByCategoryAndSaleGreaterThanOrderByLastUpdateDesc(category, 0);
		List<ProductImage> productImages = productImageRepository.findAll();

		Map<Long, List<ProductImage>> map = new HashMap<>();

		for (ProductImage productImage : productImages) {
			Long id = productImage.getProduct().getId();

			List<ProductImage> listImages = new ArrayList<>();

			if (productImage.getProduct().getId() == id) {
				listImages.add(productImage);
			}

			map.put(productImage.getProduct().getId(), listImages);
		}

		List<DataDTO> dataDTOS = new ArrayList<>();

		for (Product product : products) {
			DataDTO dataDTO = new DataDTO();

			String image = map.get(product.getId()).get(0).getImage();

			dataDTO.setId(product.getId());
			dataDTO.setProductName(product.getProductName());
			dataDTO.setIntroduce(product.getIntroduce());
			dataDTO.setDescription(product.getDescription());
			dataDTO.setPriceImport(product.getPriceImport());
			dataDTO.setPriceSell(product.getPriceSell());
			dataDTO.setQuantityImport(product.getQuantityImport());
			dataDTO.setQuantitySell(product.getQuantitySell());
			dataDTO.setInventory(product.getInventory());
			dataDTO.setSale(product.getSale());
			dataDTO.setLastUpdate(product.getLastUpdate());
			dataDTO.setImage(image);
			dataDTO.setThoroughbred(product.getThoroughbred());
			dataDTO.setCategory(product.getCategory());
			dataDTO.setUserSystem(product.getUserSystem());

			dataDTOS.add(dataDTO);
		}

		return dataDTOS;
	}

	@Override
	public List<DataDTO> findByProductLike(Long category, int sale, Long productId) {
		Optional<Category> categoryData = categoryRepository.findById(category);
		List<Product> products = new ArrayList<>();
		if (sale == 0) {
			products = productRepository
					.findTop4ByCategoryAndSaleEqualsAndIdNotOrderByLastUpdateDesc(categoryData.get(), 0, productId);
		} else {
			products = productRepository.findTop4ByCategoryAndSaleGreaterThanAndIdNotOrderByLastUpdateDesc(
					categoryData.get(), 0, productId);
		}

		List<ProductImage> productImages = productImageRepository.findAll();

		Map<Long, List<ProductImage>> map = new HashMap<>();

		for (ProductImage productImage : productImages) {
			Long id = productImage.getProduct().getId();

			List<ProductImage> listImages = new ArrayList<>();

			if (productImage.getProduct().getId() == id) {
				listImages.add(productImage);
			}

			map.put(productImage.getProduct().getId(), listImages);
		}

		List<DataDTO> dataDTOS = new ArrayList<>();

		for (Product product : products) {
			DataDTO dataDTO = new DataDTO();

			String image = map.get(product.getId()).get(0).getImage();

			dataDTO.setId(product.getId());
			dataDTO.setProductName(product.getProductName());
			dataDTO.setIntroduce(product.getIntroduce());
			dataDTO.setDescription(product.getDescription());
			dataDTO.setPriceImport(product.getPriceImport());
			dataDTO.setPriceSell(product.getPriceSell());
			dataDTO.setQuantityImport(product.getQuantityImport());
			dataDTO.setQuantitySell(product.getQuantitySell());
			dataDTO.setInventory(product.getInventory());
			dataDTO.setSale(product.getSale());
			dataDTO.setLastUpdate(product.getLastUpdate());
			dataDTO.setImage(image);
			dataDTO.setThoroughbred(product.getThoroughbred());
			dataDTO.setCategory(product.getCategory());
			dataDTO.setUserSystem(product.getUserSystem());

			dataDTOS.add(dataDTO);
		}

		return dataDTOS;
	}

}
