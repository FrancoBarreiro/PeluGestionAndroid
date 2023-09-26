package com.sandrapeinados.pelugestion.controllers;

import com.sandrapeinados.pelugestion.models.Customer;
import com.sandrapeinados.pelugestion.services.ICustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @GetMapping("/find")
    public ResponseEntity<?> getCustomersByName(@RequestParam String name) {

        List<Customer> customers = customerService.getCustomersByName(name);
        return ResponseEntity.ok(customers);
    }
    @GetMapping
    public ResponseEntity<?> getCustomers() {
        List<Customer> customers = customerService.getCustomers();
        return ResponseEntity.ok(customers);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id){
        Customer customerFound = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerFound);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getCustomerDetails(@PathVariable Long id){
        Customer customerFound = customerService.getCustomerDetails(id);
        return ResponseEntity.ok(customerFound);
    }

    @PostMapping
    public ResponseEntity<?> saveCustomer(@RequestBody @Valid Customer customer) {
        customerService.saveCustomer(customer);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customer.getId())
                .toUri();
        return ResponseEntity.created(location).body(customer);
    }

    @PutMapping
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer){
        customerService.updateCustomer(customer);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("paged")
    public ResponseEntity<?> getCustomersPaged(@RequestParam int size, @RequestParam int page){
        Page<Customer> customers = customerService.getCustomersPaged(size, page);
        return ResponseEntity.ok(customers);
    }
}
