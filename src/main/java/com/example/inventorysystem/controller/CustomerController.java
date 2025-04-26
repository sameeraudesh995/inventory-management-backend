package com.example.inventorysystem.controller;

import com.example.inventorysystem.model.Customer;
import com.example.inventorysystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

    // ⭐ New endpoint for image upload
    @PostMapping("/{id}/upload")
    public Customer uploadCustomerImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        Customer customer = customerService.getCustomerById(id);

        // Generate unique file name
        String filename = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(file.getOriginalFilename());

        // Save file to folder
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        file.transferTo(new File(UPLOAD_DIR + filename));

        // Update customer image URL
        customer.setImageUrl("/" + UPLOAD_DIR + filename);
        return customerService.saveCustomer(customer);
    }
}
