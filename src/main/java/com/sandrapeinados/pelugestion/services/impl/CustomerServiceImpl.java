package com.sandrapeinados.pelugestion.services.impl;

import com.sandrapeinados.pelugestion.exceptions.BadRequestException;
import com.sandrapeinados.pelugestion.exceptions.ResourceNotFoundException;
import com.sandrapeinados.pelugestion.models.Customer;
import com.sandrapeinados.pelugestion.models.Job;
import com.sandrapeinados.pelugestion.models.SubJob;
import com.sandrapeinados.pelugestion.persistence.entities.CustomerEntity;
import com.sandrapeinados.pelugestion.persistence.entities.JobEntity;
import com.sandrapeinados.pelugestion.persistence.entities.SubJobEntity;
import com.sandrapeinados.pelugestion.persistence.repositories.ICustomerRepository;
import com.sandrapeinados.pelugestion.persistence.repositories.IJobRepository;
import com.sandrapeinados.pelugestion.persistence.repositories.ISubJobRepository;
import com.sandrapeinados.pelugestion.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private ICustomerRepository customerRepo;
    @Autowired
    private IJobRepository jobRepo;
    @Autowired
    ISubJobRepository subJobRepo;
    String fechaPatron = "dd-MM-yyyy HH:mm:ss";
    DateTimeFormatter formateador = DateTimeFormatter.ofPattern(fechaPatron);

    @Override
    public Customer saveCustomer(Customer customer) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setName(customer.getName());
        customerEntity.setSurname(customer.getSurname());
        customerEntity.setCellphone(customer.getCellphone());
        customerRepo.save(customerEntity);

        customer.setId(customerEntity.getId());
        return customer;
    }

    @Override
    public List<Customer> getCustomers() {
        List<CustomerEntity> customersFounds = customerRepo.findAll();
        List<Customer> customerList = new ArrayList<>();

        for (CustomerEntity customerEntity : customersFounds) {
            Customer customer = new Customer();
            customer.setId(customerEntity.getId());
            customer.setName(customerEntity.getName());
            customer.setSurname(customerEntity.getSurname());
            customer.setCellphone(customerEntity.getCellphone());

            List<Job> jobsList = new ArrayList<>();
            List<JobEntity> jobsEntity = customerEntity.getJobs();

            for (JobEntity jobEntity : jobsEntity) {
                Job job = new Job();
                job.setIdClient(customer.getId());
                job.setIdJob(jobEntity.getJobId());
                job.setJobTitle(jobEntity.getJobTitle());
                job.setJobDescription(jobEntity.getJobDescription());
                job.setTotalAmount(jobEntity.getTotalAmount());
                job.setDate(jobEntity.getDate().format(formateador));

                List<SubJob> subJobsList = new ArrayList<>();
                List<SubJobEntity> subJobsEntity = jobEntity.getSubJobs();

                for (SubJobEntity subJobEntity : subJobsEntity) {
                    SubJob subJob = new SubJob();
                    subJob.setId(subJobEntity.getId());
                    subJob.setSubJobTitle(subJobEntity.getSubJobTitle());
                    subJob.setSubJobAmount(subJobEntity.getSubJobAmount());
                    subJobsList.add(subJob);
                }

                job.setSubJobs(subJobsList);
                jobsList.add(job);
            }

            customer.setJobs(jobsList);
            customerList.add(customer);
        }
        return customerList;
    }

    @Override
    public Customer getCustomerById(Long id) {
        Optional<CustomerEntity> customerFound = customerRepo.findById(id);
        if (customerFound.isPresent()) {
            CustomerEntity customerEntity = customerFound.get();

            Customer customer = new Customer();
            customer.setId(customerEntity.getId());
            customer.setName(customerEntity.getName());
            customer.setSurname(customerEntity.getSurname());
            customer.setCellphone(customerEntity.getCellphone());

            List<Job> jobsList = new ArrayList<>();
            List<JobEntity> jobsEntity = customerEntity.getJobs();

            for (JobEntity jobEntity : jobsEntity) {
                Job job = new Job();
                job.setIdClient(id);
                job.setIdJob(jobEntity.getJobId());
                job.setJobTitle(jobEntity.getJobTitle());
                job.setJobDescription(jobEntity.getJobDescription());
                job.setTotalAmount(jobEntity.getTotalAmount());
                job.setDate(jobEntity.getDate().format(formateador));

                List<SubJob> subJobsList = new ArrayList<>();
                List<SubJobEntity> subJobsEntity = jobEntity.getSubJobs();

                for (SubJobEntity subJobEntity : subJobsEntity) {
                    SubJob subJob = new SubJob();
                    subJob.setId(subJobEntity.getId());
                    subJob.setSubJobTitle(subJobEntity.getSubJobTitle());
                    subJob.setSubJobAmount(subJobEntity.getSubJobAmount());
                    subJobsList.add(subJob);
                }

                job.setSubJobs(subJobsList);
                jobsList.add(job);
            }

            customer.setJobs(jobsList);

            return customer;
        } else {
            throw new ResourceNotFoundException("The customer with id: " + id + " was not found");
        }
    }

    @Override
    public void deleteCustomer(Long id) {
        Optional<CustomerEntity> customerEntity = customerRepo.findById(id);

        if (customerEntity.isPresent()) {
            customerRepo.deleteById(customerEntity.get().getId());
        } else {
            throw new ResourceNotFoundException("Customer not found");
        }
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        Optional<CustomerEntity> customerFound = customerRepo.getOnlyDetailsCustomer(customer.getId());

        if (customerFound.isPresent()) {
            if (!customer.getName().equalsIgnoreCase("")) {
                customerRepo.updateCustomer(customer.getName(), customer.getSurname(), customer.getCellphone(), customer.getId());
                return customer;
            } else {
                throw new BadRequestException("El campo nombre no puede estar vacio");
            }
        } else {
            throw new ResourceNotFoundException("Customer not found.");
        }
    }

    /**
     * Obtener Customer sin su lista de Jobs.
     *
     * @param id indica el Id del Customer a obtener
     * @return Customer sin lista de Jobs
     */
    public Customer getCustomerDetails(Long id) {
        Optional<CustomerEntity> customerEntity = customerRepo.getOnlyDetailsCustomer(id);

        if (customerEntity.isPresent()) {
            Customer customer = new Customer();
            customer.setId(customerEntity.get().getId());
            customer.setName(customerEntity.get().getName());
            customer.setSurname(customerEntity.get().getSurname());
            customer.setCellphone(customerEntity.get().getCellphone());
            return customer;
        } else {
            throw new ResourceNotFoundException("Customer not found");
        }
    }

    @Override
    public List<Customer> getCustomersByName(String name) {
        String nameToFind = "%" + name.trim().toUpperCase() + "%";
        List<CustomerEntity> customersFounds = customerRepo.getCustomersByName(nameToFind);

        List<Customer> customerList = new ArrayList<>();

        for (CustomerEntity customerEntity : customersFounds) {
            Customer customer = new Customer();
            customer.setId(customerEntity.getId());
            customer.setName(customerEntity.getName());
            customer.setSurname(customerEntity.getSurname());
            customer.setCellphone(customerEntity.getCellphone());
            customerList.add(customer);
        }
        return customerList;
    }

    @Override
    public Page<Customer> getCustomersPaged(int size, int page) {
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CustomerEntity> customersFounds = customerRepo.findAll(pageable);
        List<Customer> customers = new ArrayList<>();

        for (CustomerEntity c : customersFounds) {
            Customer customer = new Customer();
            customer.setId(c.getId());
            customer.setName(c.getName());
            customer.setSurname(c.getSurname());
            customer.setCellphone(c.getCellphone());
            customers.add(customer);
        }
        return new PageImpl<>(customers, customersFounds.getPageable(), customersFounds.getTotalElements());
    }
}



