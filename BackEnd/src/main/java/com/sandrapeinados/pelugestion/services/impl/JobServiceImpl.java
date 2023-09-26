package com.sandrapeinados.pelugestion.services.impl;

import com.sandrapeinados.pelugestion.exceptions.BadRequestException;
import com.sandrapeinados.pelugestion.exceptions.NullPointerException;
import com.sandrapeinados.pelugestion.exceptions.ResourceNotFoundException;
import com.sandrapeinados.pelugestion.models.Customer;
import com.sandrapeinados.pelugestion.models.Job;
import com.sandrapeinados.pelugestion.models.SubJob;
import com.sandrapeinados.pelugestion.persistence.entities.CustomerEntity;
import com.sandrapeinados.pelugestion.persistence.entities.JobEntity;
import com.sandrapeinados.pelugestion.persistence.entities.SubJobEntity;
import com.sandrapeinados.pelugestion.persistence.repositories.IJobRepository;
import com.sandrapeinados.pelugestion.persistence.repositories.ISubJobRepository;
import com.sandrapeinados.pelugestion.services.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class JobServiceImpl implements IJobService {

    @Autowired
    private IJobRepository jobRepo;
    @Autowired
    private ISubJobRepository subJobRepo;
    String fechaPatron = "dd-MM-yyyy HH:mm:ss";
    DateTimeFormatter formateador = DateTimeFormatter.ofPattern(fechaPatron);


    @Override
    public Job saveJob(Job job) {

        if (Objects.nonNull(job.getIdClient()) && Objects.nonNull(job.getJobTitle()) &&
                Objects.nonNull(job.getDate()) && Objects.nonNull(job.getTotalAmount())) {
            Customer customer = new Customer();
            customer.setId(job.getIdClient());
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(customer.getId());

            List<SubJob> list = job.getSubJobs();
            List<SubJobEntity> listSubJobsEntity = new ArrayList<>();
            double totalAmount = 0;
            //Convierte la lista de SubJob a SubJobEntity y va sumando los montos
            for (SubJob s : list) {
                SubJobEntity subJobEntity = new SubJobEntity();
                subJobEntity.setSubJobTitle(s.getSubJobTitle());
                subJobEntity.setSubJobAmount(s.getSubJobAmount());
                listSubJobsEntity.add(subJobEntity);
                totalAmount += subJobEntity.getSubJobAmount();
            }


            JobEntity jobToSave = new JobEntity();

            jobToSave.setJobTitle(job.getJobTitle());
            jobToSave.setJobDescription(job.getJobDescription());
            jobToSave.setTotalAmount(totalAmount);
            jobToSave.setDate(LocalDateTime.parse(job.getDate(), formateador));
            jobToSave.setCustomerEntity(customerEntity);
            //Se guarda primero el Job sin la lista de SubJobs porque necesita el Id del Job y JPA no lo está tomando
            jobRepo.save(jobToSave);

            //Agrega el Job a las SubJobs
            for (SubJobEntity s : listSubJobsEntity) {
                s.setJob(jobToSave);
            }
            //Se guarda la lista de SubJobs correctamente con el Id del Job que las vincula
            subJobRepo.saveAll(listSubJobsEntity);

            //Setea el Id de cada SubJob
            for (int i = 0; i < listSubJobsEntity.size(); i++) {
                list.get(i).setId(listSubJobsEntity.get(i).getId());
            }

            job.setIdJob(jobToSave.getJobId());
            job.setSubJobs(list);

            return job;
        } else {
            throw new NullPointerException("Completar los campos obligatorios");
        }


    }

    @Override
    public void deleteJob(Long id) {
        Job job = getJobById(id);
        jobRepo.deleteById(job.getIdJob());
    }


    @Override
    public Job getJobById(Long id) {
        Optional<JobEntity> jobFound = jobRepo.findById(id);

        if (jobFound.isPresent()) {
            Job job = new Job();
            List<SubJob> auxilirySubJobsList = new ArrayList<>();

            job.setIdClient(jobFound.get().getCustomerEntity().getId());
            job.setIdJob(jobFound.get().getJobId());
            job.setJobTitle(jobFound.get().getJobTitle());
            job.setJobDescription(jobFound.get().getJobDescription());
            job.setDate(jobFound.get().getDate().format(formateador));
            job.setTotalAmount(jobFound.get().getTotalAmount());

            List<SubJobEntity> subJobEntityList = jobFound.get().getSubJobs();
            for (SubJobEntity sub : subJobEntityList) {
                SubJob subJob = new SubJob();
                subJob.setId(sub.getId());
                subJob.setSubJobTitle(sub.getSubJobTitle());
                subJob.setSubJobAmount(sub.getSubJobAmount());
                auxilirySubJobsList.add(subJob);
            }
            job.setSubJobs(auxilirySubJobsList);
            return job;
        } else {
            throw new ResourceNotFoundException("Job Not Found");
        }
    }

    @Override
    public List<Job> getAllJobs() {
        List<JobEntity> jobEntityList = jobRepo.findAll();
        List<Job> jobsList = new ArrayList<>();

        for (JobEntity jobEntity : jobEntityList) {
            Job job = new Job();
            job.setIdClient(jobEntity.getCustomerEntity().getId());
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
        return jobsList;
    }

    @Transactional
    @Override
    public Job updateJob(Job job) {
        Optional<JobEntity> jobFound = jobRepo.findById(job.getIdJob());

        if (jobFound.isPresent()) {
            List<SubJobEntity> subJobsToUpdate = new ArrayList<>();

            for (SubJob s : job.getSubJobs()) {
                SubJobEntity subJobEntity = new SubJobEntity();
                subJobEntity.setId(s.getId());
                subJobEntity.setSubJobTitle(s.getSubJobTitle());
                subJobEntity.setSubJobAmount(s.getSubJobAmount());
                subJobEntity.setJob(jobFound.get());
                subJobsToUpdate.add(subJobEntity);
            }
            jobFound.get().setJobTitle(job.getJobTitle());
            jobFound.get().setJobDescription(job.getJobDescription());
            jobFound.get().setDate(LocalDateTime.parse(job.getDate(), formateador));
            jobFound.get().setTotalAmount(job.getTotalAmount());
            jobFound.get().getSubJobs().clear();
            jobFound.get().getSubJobs().addAll(subJobsToUpdate);
            jobRepo.save(jobFound.get());
            return job;
        } else {
            throw new ResourceNotFoundException("Job not found");
        }
    }

    @Override
    public Page<Job> getJobsPaged(Pageable pageable) {
        Page<JobEntity> jobsFounds = jobRepo.findAll(pageable);
        List<Job> jobs = new ArrayList<>();
        for (JobEntity jobEntity : jobsFounds.getContent()) {
            Job job = new Job();
            job.setIdClient(jobEntity.getCustomerEntity().getId());
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
            jobs.add(job);
        }
        return new PageImpl<>(jobs, jobsFounds.getPageable(), jobsFounds.getTotalElements());
    }

    @Override
    public Page<Job> findJobsBetweenDates(String from, String to, int page, int size) {
        LocalDateTime findFrom = LocalDateTime.parse(from, formateador);
        LocalDateTime findTo = LocalDateTime.parse(to, formateador);
        LocalDate fromDate = findFrom.toLocalDate();
        LocalDate toDate = findTo.toLocalDate();

        Sort sort = Sort.by("date").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (fromDate.isBefore(toDate) || fromDate.equals(toDate)) {
            Page<JobEntity> jobsFounds = jobRepo.findJobsBetweenDates(findFrom, findTo, pageable);
            List<Job> jobs = new ArrayList<>();
            for (JobEntity jobEntity : jobsFounds.getContent()) {
                Job job = new Job();
                job.setIdClient(jobEntity.getCustomerEntity().getId());
                job.setIdJob(jobEntity.getJobId());
                job.setJobTitle(jobEntity.getJobTitle());
                job.setJobDescription(jobEntity.getJobDescription());
                job.setTotalAmount(jobEntity.getTotalAmount());
                job.setDate(jobEntity.getDate().format(formateador));

                // Obtener los nombres y apellidos del cliente
                CustomerEntity customerEntity = jobEntity.getCustomerEntity();
                job.setCustomerName(customerEntity.getName());
                job.setCustomerSurname(customerEntity.getSurname());

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
                jobs.add(job);
            }
            return new PageImpl<>(jobs, jobsFounds.getPageable(), jobsFounds.getTotalElements());
        } else {
            throw new BadRequestException("The 'From' date cannot be greater than the 'To' date.");
        }
    }

   /* @Override
    public Page<Job> findJobsBetweenDates(String from, String to, int page, int size) {

        LocalDateTime findFrom = LocalDateTime.parse(from, formateador);
        LocalDateTime findTo = LocalDateTime.parse(to, formateador);
        LocalDate fromDate = findFrom.toLocalDate();
        LocalDate toDate = findTo.toLocalDate();

        Sort sort = Sort.by("date").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (fromDate.isBefore(toDate) || fromDate.equals(toDate)) {
            Page<JobEntity> jobsFounds = jobRepo.findJobsBetweenDates(findFrom, findTo, pageable);
            List<Job> jobs = new ArrayList<>();
            for (JobEntity jobEntity : jobsFounds.getContent()) {
                Job job = new Job();
                job.setIdClient(jobEntity.getCustomerEntity().getId());
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
                jobs.add(job);
            }
            return new PageImpl<>(jobs, jobsFounds.getPageable(), jobsFounds.getTotalElements());
        } else {
            throw new BadRequestException("The 'From' date cannot be greater than the 'To' date.");
        }
    }*/
    @Override
    public double getSumTotalJobsByDates(String dateFrom, String dateTo) {
        LocalDateTime from = LocalDateTime.parse(dateFrom, formateador);
        LocalDateTime to = LocalDateTime.parse(dateTo, formateador);
        Optional<Double> sumOptional = jobRepo.getSumTotal(from, to);
        double sum = sumOptional.orElse(0.0);
        return sum;
    }

    @Override
    public Page<Job> getJobsPagedByCustomerId(Long id, Pageable pageable) {
        Page<JobEntity> jobsFounds = jobRepo.findJobsByCustomerId(id, pageable);
        List<Job> jobs = new ArrayList<>();
        for (JobEntity jobEntity : jobsFounds.getContent()) {
            Job job = new Job();
            job.setIdClient(jobEntity.getCustomerEntity().getId());
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
            jobs.add(job);
        }
        return new PageImpl<>(jobs, jobsFounds.getPageable(), jobsFounds.getTotalElements());
    }
}
