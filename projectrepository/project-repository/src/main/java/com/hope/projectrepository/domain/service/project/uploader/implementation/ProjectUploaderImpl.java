package com.hope.projectrepository.domain.service.project.uploader.implementation;

import com.hope.projectrepository.exception.service.project.ProjectOverviewDoesNotExistException;
import com.hope.projectrepository.util.dto.ProjectContentDTO;
import com.hope.projectrepository.util.dto.ProjectDTO;
import com.hope.projectrepository.domain.entity.ProjectContent;
import com.hope.projectrepository.domain.entity.ProjectOverview;
import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.domain.repository.ProjectContentRepository;
import com.hope.projectrepository.domain.repository.ProjectOverviewRepository;
import com.hope.projectrepository.domain.service.project.file.FileManager;
import com.hope.projectrepository.domain.service.project.uploader.ProjectUploader;
import com.hope.projectrepository.util.global.ContextManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ProjectUploaderImpl implements ProjectUploader {
    @Autowired
    ProjectOverviewRepository projectOverviewRepository;
    @Autowired
    ProjectContentRepository projectContentRepository;

    @Autowired
    FileManager fileManager;

    @Transactional
    public ProjectOverview uploadProject(ProjectDTO projectDTO, MultipartFile[] files){
        User user = ContextManager.getCurrentUser();

        ProjectOverview projectOverview = saveNewProjectOverview(projectDTO, user);
        saveProjectContentAndFiles(projectDTO, files, projectOverview);

        return projectOverview;
    }

    private ProjectOverview saveNewProjectOverview(ProjectDTO projectDTO, User user){
        String title = projectDTO.getTitle();
        String subject = projectDTO.getSubject();
        String scale = projectDTO.getScale();
        String techstack = projectDTO.getTechstack();
        String startDate =projectDTO.getProjectStartDate();
        String endDate = projectDTO.getProjectEndDate();

        ProjectOverview newOverview = ProjectOverview.builder()
                .user(user)
                .mainTitle(title)
                .subject(subject)
                .scale(scale)
                .techStack(techstack)
                .projectStartDate(parseDate(startDate))
                .projectEndDate(parseDate(endDate))
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        projectOverviewRepository.save(newOverview);

        return newOverview;
    }

    private LocalDate parseDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date,formatter);
    }

    private void saveProjectContentAndFiles(ProjectDTO projectDTO, MultipartFile[] files, ProjectOverview projectOverview){
        ProjectContentDTO[] contentDTOS = projectDTO.getContentDTOS();

        for(int i = 0; i < contentDTOS.length; i++){
            ProjectContent projectContent = saveProjectContent(contentDTOS[i], projectOverview);
            if(isFileExist(files[i]))
                fileManager.uploadFile(projectContent, files[i]);
        }
    }

    private ProjectContent saveProjectContent(ProjectContentDTO contentDTO, ProjectOverview projectOverview){
        ProjectContent newContent = ProjectContent.builder()
                .projectOverview(projectOverview)
                .content(parseNewLine(contentDTO.getContent()))
                .subTitle(contentDTO.getContentTitle())
                .no(contentDTO.getNo())
                .build();

        projectContentRepository.save(newContent);

        return newContent;
    }

    private String parseNewLine(String str){
        return str.replace("\n","<br/>");
    }

    private boolean isFileExist(MultipartFile file){
        if(file.getOriginalFilename().equals("__dummy"))
            return false;
        return true;
    }

    public ProjectOverview updateProject(ProjectDTO projectDTO, MultipartFile[] files, String projectId){
        ProjectOverview projectOverview = findProjectOverviewById(projectId);
        updateProjectOverview(projectOverview, projectDTO);

        deleteOriginContents(projectOverview);
        saveProjectContentAndFiles(projectDTO, files, projectOverview);

        return projectOverview;
    }

    private ProjectOverview findProjectOverviewById(String projectId){
        Optional<ProjectOverview> optProjectOverview = projectOverviewRepository.findById(Long.parseLong(projectId));
        ProjectOverview projectOverview = optProjectOverview.get();

        if(projectOverview == null)
            throw new ProjectOverviewDoesNotExistException();

        return projectOverview;
    }

    private void updateProjectOverview(ProjectOverview projectOverview, ProjectDTO projectDTO){
        String title = projectDTO.getTitle();
        String subject = projectDTO.getSubject();
        String scale = projectDTO.getScale();
        String techstack = projectDTO.getTechstack();
        String startDate =projectDTO.getProjectStartDate();
        String endDate = projectDTO.getProjectEndDate();

        projectOverview.setMainTitle(title);
        projectOverview.setSubject(subject);
        projectOverview.setScale(scale);
        projectOverview.setTechStack(techstack);
        projectOverview.setProjectStartDate(parseDate(startDate));
        projectOverview.setProjectEndDate(parseDate(endDate));
        projectOverview.setUpdateDate(LocalDateTime.now());

        projectOverviewRepository.save(projectOverview);
    }

    private void deleteOriginContents(ProjectOverview projectOverview){
        List<ProjectContent> projectContentList = projectContentRepository.findByProjectOverview(projectOverview);

        for(ProjectContent projectContent: projectContentList){
            if(projectContent != null){
                fileManager.deleteFile(projectContent);
                projectContentRepository.delete(projectContent);
            }
        }
    }
}
