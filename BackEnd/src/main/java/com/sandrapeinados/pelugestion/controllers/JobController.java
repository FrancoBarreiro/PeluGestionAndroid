package com.sandrapeinados.pelugestion.controllers;

import com.sandrapeinados.pelugestion.models.Job;
import com.sandrapeinados.pelugestion.services.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private IJobService jobService;

    @PostMapping
    public ResponseEntity<?> saveJob(@RequestBody Job job) {
        jobService.saveJob(job);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(job.getIdJob())
                .toUri();

        return ResponseEntity.created(location).body(job);
    }
    @GetMapping
    public ResponseEntity<?> getJobs(){
        List<Job> jobList = jobService.getAllJobs();
        return ResponseEntity.ok(jobList);
    }

    @GetMapping("/view")
    public ResponseEntity<?> getJobsPaged(@RequestParam int page, @RequestParam int size, @RequestParam String by) {
        Sort sort = Sort.by(by).ascending();
        Pageable pageable = PageRequest.of(page,size,sort);
        Page<Job> jobsList = jobService.getJobsPaged(pageable);
        return  ResponseEntity.ok(jobsList);
    }

    @GetMapping("/results")
    public ResponseEntity<?> getJobsPageds(@RequestParam int page, @RequestParam int size,
                                           @RequestParam String from, @RequestParam String to) {
        Page<Job> jobsList = jobService.findJobsBetweenDates(from, to, page, size);
        return  ResponseEntity.ok(jobsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id){
        Job job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id){
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping
    public ResponseEntity<?> updateJob(@RequestBody Job job){
        jobService.updateJob(job);
        return ResponseEntity.ok(job);
    }
    @GetMapping("/totalByPeriod")
    public ResponseEntity<?> getSumTotal(@RequestParam String from, @RequestParam String to){
        double sum = jobService.getSumTotalJobsByDates(from,to);
        return ResponseEntity.ok(sum);
    }

    @GetMapping("/jobs-customer/{id}")
    public ResponseEntity<?> getJobsByCustomerId(@RequestParam int page, @RequestParam int size,
                                                 @PathVariable Long id) {
        Sort sort = Sort.by("date").descending();
        Pageable pageable = PageRequest.of(page,size,sort);
        Page<Job> jobsList = jobService.getJobsPagedByCustomerId(id,pageable);
        return  ResponseEntity.ok(jobsList);
    }
}
