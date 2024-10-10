package com.wevioo.fgdb.extract.batch.controller;

import com.wevioo.fgdb.common.constants.ApplicationConstants;
import com.wevioo.fgdb.common.exception.AlreadyExistException;
import com.wevioo.fgdb.common.exception.BadRequestException;
import com.wevioo.fgdb.common.exception.ConflictException;
import com.wevioo.fgdb.common.exception.DataNotFoundException;
import com.wevioo.fgdb.common.exception.ForbiddenException;
import com.wevioo.fgdb.common.exception.UnauthorizedException;
import com.wevioo.fgdb.extract.batch.configuration.Directory;
import com.wevioo.fgdb.extract.batch.configuration.DirectoryService;
import com.wevioo.fgdb.extract.batch.configuration.DynamicValidationService;
import generated.Vuc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigInteger;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class BatchJobController {

    @Autowired
    private DynamicValidationService dynamicValidationService;


    @Autowired
    private DirectoryService directoryService;


    JobLauncher jobLauncher;
    JobOperator jobOperator;
    Job parallelJob;
    @Autowired
    public BatchJobController(JobLauncher jobLauncher,JobOperator jobOperator,
                           @Qualifier("parallelJob") Job job
    ) {
        this.jobLauncher = jobLauncher;
        this.jobOperator = jobOperator;
        this.parallelJob = job;
    }
    @GetMapping("/load")
    @CrossOrigin
    public  String handle() throws Exception {
        // Get the selected directory
        List<Directory> stopRepertories = directoryService.getListDirectory();
        if (stopRepertories.isEmpty()) {
            throw new IllegalStateException("No directories with status STOP found.");
        }
        Random random = new Random();
        Directory selectedRepertory = stopRepertories.get(random.nextInt(stopRepertories.size()));


        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("currentTime", System.currentTimeMillis())
                .addLong("idRepository", selectedRepertory.getId())
                .addString("pathRepository", selectedRepertory.getPath())
                .toJobParameters();
        jobLauncher.run(parallelJob, jobParameters);
        return  "batch  finished.....!";
    }


    // Trigger BadRequestException
    @GetMapping("/test/bad-request")
    public ResponseEntity<String> throwBadRequestException() {
        throw new BadRequestException(ApplicationConstants.ERROR, "This is a bad request exception");
    }

    // Trigger ConflictException
    @GetMapping("/test/conflict")
    public ResponseEntity<String> throwConflictException() {
        throw new ConflictException(ApplicationConstants.ERROR, "Conflict error");
    }

    // Trigger UnauthorizedException
    @GetMapping("/test/unauthorized")
    public ResponseEntity<String> throwUnauthorizedException() {
        throw new UnauthorizedException(ApplicationConstants.ERROR_UNAUTHORIZED_REQUEST, "Unauthorized access");
    }

    // Trigger AlreadyExistException
    @GetMapping("/test/already-exist")
    public ResponseEntity<String> throwAlreadyExistException() {
        throw new AlreadyExistException(ApplicationConstants.ERROR, "This entity already exists");
    }

    // Trigger DataNotFoundException
    @GetMapping("/test/data-not-found")
    public ResponseEntity<String> throwDataNotFoundException() {
        throw new DataNotFoundException(ApplicationConstants.ERROR, "Data not found");
    }

    // Trigger ForbiddenException
    @GetMapping("/test/forbidden")
    public ResponseEntity<String> throwForbiddenException() {
        throw new ForbiddenException(ApplicationConstants.ERROR_FORBIDDEN_REQUEST, "Access is forbidden");
    }

    @GetMapping("/validate")
    public void  processPerson(Vuc vuc){
        Vuc.Depositors.Depositor depositor = new Vuc.Depositors.Depositor();
        Vuc.Depositors.Depositor.DepositorIdentificationPp DepositorIdentificationPp  = new  Vuc.Depositors.Depositor.DepositorIdentificationPp();
        DepositorIdentificationPp.setCinNum(new BigInteger("12345678"));
        DepositorIdentificationPp.setPassportNum(null);
        DepositorIdentificationPp.setResidenceCertificate(null);
        DepositorIdentificationPp.setNationality(new BigInteger("788"));
        DepositorIdentificationPp.setCinIssueDate("07/01/2050");
        depositor.setDepositorIdentificationPp(DepositorIdentificationPp);

        Vuc.Depositors.Depositor depositor1 = new Vuc.Depositors.Depositor();
        Vuc.Depositors.Depositor.DepositorIdentificationPp DepositorIdentificationPp1  = new  Vuc.Depositors.Depositor.DepositorIdentificationPp();
        DepositorIdentificationPp1.setCinNum(new BigInteger("12345679"));
        DepositorIdentificationPp1.setPassportNum(null);
        DepositorIdentificationPp1.setResidenceCertificate(null);
        DepositorIdentificationPp1.setNationality(new BigInteger("789"));
        DepositorIdentificationPp1.setCinIssueDate("07/01/2050");
        depositor1.setDepositorIdentificationPp(DepositorIdentificationPp1);


        Vuc.Depositors depositors  = new Vuc.Depositors();
        depositors.getDepositor().add(depositor);
        depositors.getDepositor().add(depositor1);
        vuc.setDepositors(depositors);
        dynamicValidationService.validate(vuc.getDepositors().getDepositor());
    }
}
