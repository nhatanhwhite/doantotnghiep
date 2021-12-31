package com.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.entity.Category;
import com.entity.Product;
import com.entity.ProductImage;
import com.payload.MessageResponse;
import com.service.CategoryService;
import com.service.ProductImageService;
import com.service.ProductService;
import com.service.dto.DataDTO;
import com.service.dto.ProductDTO;
import com.utils.FileStorageUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.http.HttpHeaders;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/product")
public class ProductController {

    @Value("${app.image.url}")
    private String folder;
    
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductImageService productImageService;

    @PostMapping("/save")
    public ResponseEntity<MessageResponse> save(@RequestParam("product") String product, @RequestParam("category") String category, @RequestParam("thoroughbred") String thoroughbred, @RequestParam("files") List<MultipartFile> files) {

        Product result = productService.save(product, category, thoroughbred, files);

        if (result != null) {
            return ResponseEntity.ok(new MessageResponse("success"));
        }

        return ResponseEntity.ok(new MessageResponse("failed"));
    }

    @GetMapping("/find-id/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        Product product = productService.findById(id);

        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<DataDTO>> findAll() {
        List<DataDTO> dataDTOS = productService.findAll2();

        for(DataDTO dataDTO : dataDTOS) {
            String url = MvcUriComponentsBuilder
            .fromMethodName(NewsController.class, "getFile", dataDTO.getImage().toString()).build().toString();

            dataDTO.setImage(url);
        }
        
        return ResponseEntity.ok().body(dataDTOS);
    }

    @PutMapping("/update")
    public ResponseEntity<MessageResponse> update(@RequestParam("product") String product, @RequestParam("category") String category, @RequestParam("thoroughbred") String thoroughbred) {
        Product result = productService.update(product, category, thoroughbred);

        if (result != null) {
            return ResponseEntity.ok(new MessageResponse("success"));
        }

        return ResponseEntity.ok(new MessageResponse("failed"));
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        FileStorageUtils fileStoreUtil = new FileStorageUtils();
        Path root = Paths.get(folder);

        Resource file = fileStoreUtil.loadFile(root, filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<ProductDTO> view(@PathVariable Long id) {
        Product product = productService.findById(id);

        List<ProductImage> productImages = productImageService.findByProduct(product);

        for(ProductImage productImage : productImages) {
            String url = MvcUriComponentsBuilder
            .fromMethodName(NewsController.class, "getFile", productImage.getImage().toString()).build().toString();

            productImage.setImage(url);
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setId(product.getId());
        productDTO.setProductName(product.getProductName());
        productDTO.setIntroduce(product.getIntroduce());
        productDTO.setDescription(product.getDescription());
        productDTO.setPriceImport(product.getPriceImport());
        productDTO.setPriceSell(product.getPriceSell());
        productDTO.setQuantityImport(product.getQuantityImport());
        productDTO.setQuantitySell(product.getQuantitySell());
        productDTO.setInventory(product.getInventory());
        productDTO.setSale(product.getSale());
        productDTO.setLastUpdate(product.getLastUpdate());
        productDTO.setProductImages(productImages);
        productDTO.setThoroughbred(product.getThoroughbred());
        productDTO.setCategory(product.getCategory());
        productDTO.setUserSystem(product.getUserSystem());

        return ResponseEntity.ok().body(productDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        try{
            productService.delete(id);

            return ResponseEntity.ok(new MessageResponse("success"));
        } catch(Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(new MessageResponse("failed"));
    }

    @PostMapping("/update-image")
    public ResponseEntity<MessageResponse> updateImage(@RequestParam("id") String id, @RequestParam("files") List<MultipartFile> files) {

        Product result = productService.updateImage(id, files);

        if (result != null) {
            return ResponseEntity.ok(new MessageResponse("success"));
        }

        return ResponseEntity.ok(new MessageResponse("failed"));
    }

    @GetMapping("/all/product-newest")
    public ResponseEntity<List<DataDTO>> findTop8ProductNews() {
        List<DataDTO> dataDTOS = productService.findTop8ProductNews();

        for(DataDTO dataDTO : dataDTOS) {
            String url = MvcUriComponentsBuilder
            .fromMethodName(NewsController.class, "getFile", dataDTO.getImage().toString()).build().toString();

            dataDTO.setImage(url);
        }

        return ResponseEntity.ok().body(dataDTOS);
    }

    @GetMapping("/all/product-sale")
    public ResponseEntity<List<DataDTO>> findTop8ProductSale() {
        List<DataDTO> dataDTOS = productService.findTop8ProductSale();

        for(DataDTO dataDTO : dataDTOS) {
            String url = MvcUriComponentsBuilder
            .fromMethodName(NewsController.class, "getFile", dataDTO.getImage().toString()).build().toString();

            dataDTO.setImage(url);
        }

        return ResponseEntity.ok().body(dataDTOS);
    }

    @GetMapping("/all/product-category/{id}")
    public ResponseEntity<List<DataDTO>> findByCategory(@PathVariable Long id) {
        Category category = categoryService.findById(id);

        List<DataDTO> dataDTOS = productService.findByCategory(category);

        for(DataDTO dataDTO : dataDTOS) {
            String url = MvcUriComponentsBuilder
            .fromMethodName(NewsController.class, "getFile", dataDTO.getImage().toString()).build().toString();

            dataDTO.setImage(url);
        }

        return ResponseEntity.ok().body(dataDTOS);
    }

    @GetMapping("/all/product-category-sale/{id}")
    public ResponseEntity<List<DataDTO>> findByCategoryAndSale(@PathVariable Long id) {
        Category category = categoryService.findById(id);

        List<DataDTO> dataDTOS = productService.findByCategoryAndSale(category);

        for(DataDTO dataDTO : dataDTOS) {
            String url = MvcUriComponentsBuilder
            .fromMethodName(NewsController.class, "getFile", dataDTO.getImage().toString()).build().toString();

            dataDTO.setImage(url);
        }

        return ResponseEntity.ok().body(dataDTOS);
    }

    @GetMapping("/all/detail/{id}")
    public ResponseEntity<ProductDTO> productDetail(@PathVariable Long id) {
        Product product = productService.findById(id);

        List<ProductImage> productImages = productImageService.findByProduct(product);

        for(ProductImage productImage : productImages) {
            String url = MvcUriComponentsBuilder
            .fromMethodName(NewsController.class, "getFile", productImage.getImage().toString()).build().toString();

            productImage.setImage(url);
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setId(product.getId());
        productDTO.setProductName(product.getProductName());
        productDTO.setIntroduce(product.getIntroduce());
        productDTO.setDescription(product.getDescription());
        productDTO.setPriceImport(product.getPriceImport());
        productDTO.setPriceSell(product.getPriceSell());
        productDTO.setQuantityImport(product.getQuantityImport());
        productDTO.setQuantitySell(product.getQuantitySell());
        productDTO.setInventory(product.getInventory());
        productDTO.setSale(product.getSale());
        productDTO.setLastUpdate(product.getLastUpdate());
        productDTO.setProductImages(productImages);
        productDTO.setThoroughbred(product.getThoroughbred());
        productDTO.setCategory(product.getCategory());
        productDTO.setUserSystem(product.getUserSystem());

        return ResponseEntity.ok().body(productDTO);
    }

    @GetMapping("/all/product-like")
    public ResponseEntity<List<DataDTO>> findByProductLike(@RequestParam("categoryId") String categoryId, @RequestParam("sale") String sale, @RequestParam("productId") String productId) {
        Long category = Long.parseLong(categoryId);
        int salePercent = Integer.parseInt(sale);
        Long product = Long.parseLong(productId);

        List<DataDTO> dataDTOS = productService.findByProductLike(category, salePercent, product);

        for(DataDTO dataDTO : dataDTOS) {
            String url = MvcUriComponentsBuilder
            .fromMethodName(NewsController.class, "getFile", dataDTO.getImage().toString()).build().toString();

            dataDTO.setImage(url);
        }

        return ResponseEntity.ok().body(dataDTOS);
    }
}
