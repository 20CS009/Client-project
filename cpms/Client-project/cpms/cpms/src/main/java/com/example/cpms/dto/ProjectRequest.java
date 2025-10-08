//package com.example.cpms.dto;
//
//import com.example.cpms.entity.ProjectStatus;
//import jakarta.validation.constraints.NotBlank;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.LocalDate;
//
//@Getter
//@Setter
//public class ProjectRequest {
//
//    @NotBlank(message = "Project title is required")
//    private String title;
//
//    private String description;
//
//    private LocalDate startDate;
//    private LocalDate endDate;
//
//    private ProjectStatus status = ProjectStatus.PLANNED;
//
//    @NotBlank(message = "Client ID is required")
//    private Long clientId;
//}


package com.example.cpms.dto;

import com.example.cpms.entity.ProjectStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectRequest {

    @NotBlank(message = "Project title is required")
    private String title;

    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    private ProjectStatus status = ProjectStatus.PLANNED;

    @NotNull(message = "Client ID is required")
    private Long clientId;
}