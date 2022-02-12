package com.flamexander.cloud.service.product;

import com.flamexander.cloud.common.ProductDto;
import com.flamexander.cloud.service.product.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@CrossOrigin("*") // Homework: *
public class ProductController {
    private final ProductService productService;
//    private final ProductConverter productConverter;
//    private final ProductValidator productValidator;

    private static final Function<Product, ProductDto> mapper = p -> new ProductDto(p.getId(), p.getTitle(), p.getPrice());


    @GetMapping
    public List<ProductDto> getAllProducts(
            @RequestParam(name = "min_price", required = false) Integer minPrice,
            @RequestParam(name = "max_price", required = false) Integer maxPrice,
            @RequestParam(name = "title_part", required = false) String titlePart
    ) {

        return productService.findAll(minPrice, maxPrice, titlePart).stream().map(p -> mapper.apply(p)).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        Product product = productService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found, id: " + id));
        return mapper.apply(product);
    }

//    @PostMapping
//    public ProductDto saveNewProduct(@RequestBody ProductDto productDto) {
////        productValidator.validate(productDto);
//        Product product = productConverter.dtoToEntity(productDto);
//        product = productService.save(product);
//        return mapper.apply(product);
//    }

    @PutMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
//        productValidator.validate(productDto);
        Product product = productService.update(productDto);
        return mapper.apply(product);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        productService.deleteById(id);
    }
}

