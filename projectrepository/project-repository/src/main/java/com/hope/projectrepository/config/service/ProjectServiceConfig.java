
package com.hope.projectrepository.config.service;

import com.hope.projectrepository.domain.service.project.ProjectService;
import com.hope.projectrepository.domain.service.project.comparator.ProjectContentComparator;
import com.hope.projectrepository.domain.service.project.comparator.ProjectOverviewComparator;
import com.hope.projectrepository.domain.service.project.comparator.implementation.ProjectContentComparatorImpl;
import com.hope.projectrepository.domain.service.project.comparator.implementation.ProjectOverviewComparatorImpl;
import com.hope.projectrepository.domain.service.project.file.FileManager;
import com.hope.projectrepository.domain.service.project.file.implementation.FileManagerImpl;
import com.hope.projectrepository.domain.service.project.finder.ProjectFinder;
import com.hope.projectrepository.domain.service.project.finder.implementation.ProjectFinderImpl;
import com.hope.projectrepository.domain.service.project.implementation.ProjectServiceImpl;
import com.hope.projectrepository.domain.service.project.uploader.ProjectUploader;
import com.hope.projectrepository.domain.service.project.uploader.implementation.ProjectUploaderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectServiceConfig {
    @Bean
    public ProjectContentComparator getProjectContentComparator(){
        return new ProjectContentComparatorImpl();
    }

    @Bean
    public ProjectOverviewComparator getProjectOverviewComparator(){
        return new ProjectOverviewComparatorImpl();
    }

    @Bean
    public ProjectService getProjectService(){
        return new ProjectServiceImpl();
    }

    @Bean
    public ProjectFinder getProjectFinder() { return new ProjectFinderImpl(); }

    @Bean
    public ProjectUploader getProjectUploader() { return new ProjectUploaderImpl(); }

    @Bean
    public FileManager getFileService(){ return new FileManagerImpl(); }
}
