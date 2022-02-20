package com.hope.projectrepository.domain.service.project.finder.implementation;

import com.hope.projectrepository.util.dto.ProjectContentAndFileInfoDTO;
import com.hope.projectrepository.domain.entity.FileInfo;
import com.hope.projectrepository.domain.entity.ProjectContent;
import com.hope.projectrepository.domain.entity.ProjectOverview;
import com.hope.projectrepository.domain.repository.FileInfoRepository;
import com.hope.projectrepository.domain.repository.ProjectContentRepository;
import com.hope.projectrepository.domain.repository.ProjectOverviewRepository;
import com.hope.projectrepository.domain.service.project.comparator.ProjectContentComparator;
import com.hope.projectrepository.domain.service.project.comparator.ProjectOverviewComparator;
import com.hope.projectrepository.domain.service.project.finder.ProjectFinder;
import com.hope.projectrepository.exception.service.project.ProjectOverviewDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

public class ProjectFinderImpl implements ProjectFinder {
    @Autowired
    ProjectOverviewRepository projectOverviewRepository;
    @Autowired
    ProjectContentRepository projectContentRepository;
    @Autowired
    FileInfoRepository fileInfoRepository;

    @Autowired
    ProjectContentComparator projectContentComparator;
    @Autowired
    ProjectOverviewComparator projectOverviewComparator;

    @Value("${app.controller.params.project.dto.title_and_content}")
    String TITLE_AND_CONTENT;
    @Value("${app.controller.params.project.dto.title}")
    String TITLE;
    @Value("${app.controller.params.project.dto.content}")
    String CONTENT;
    @Value("${app.controller.params.project.dto.nickname}")
    String NICKNAME;
    @Value("${app.controller.params.project.dto.techstack}")
    String TECHSTACK;
    @Value("${app.controller.params.project.dto.start_date}")
    String START_DATE;
    @Value("${app.controller.params.project.dto.end_date}")
    String END_DATE;

    @Value("${app.controller.params.project.default}")
    String NONE;

    public ProjectOverview getProjectOverview(String projectId){
        Optional<ProjectOverview> optionalProjectOverview = projectOverviewRepository.findById(Long.parseLong(projectId));

        if(optionalProjectOverview.equals(Optional.empty()))
            throw new ProjectOverviewDoesNotExistException();

        ProjectOverview projectOverview = optionalProjectOverview.get();

        return projectOverview;
    }

    public List<ProjectContentAndFileInfoDTO> getProjectContentAndFiles(ProjectOverview projectOverview){

        List<ProjectContent> contents = getProjectContentByProjectOverview(projectOverview);
        sortProjectContents(contents);

        List<ProjectContentAndFileInfoDTO> contentAndFiles = constructContentAndFiles(contents);
        return contentAndFiles;
    }

    private List<ProjectContent> getProjectContentByProjectOverview(ProjectOverview projectOverview){
        return projectContentRepository.findByProjectOverview(projectOverview);
    }

    private void sortProjectContents(List<ProjectContent> contents){
        contents.sort(projectContentComparator.basicOrder());
    }

    private List<ProjectContentAndFileInfoDTO> constructContentAndFiles(List<ProjectContent> contents){
        List<ProjectContentAndFileInfoDTO> contentAndFiles = new ArrayList<>();
        for(ProjectContent content: contents){
            FileInfo file = fileInfoRepository.findByProjectContent(content);
            contentAndFiles.add(new ProjectContentAndFileInfoDTO(content, file));
        }
        return contentAndFiles;
    }

    public List<ProjectOverview> search(String category, String orderby, String text){
        List<ProjectOverview> result = searchByCategory(category, text);
        sortProjectOverviews(result, orderby);

        return result;
    }

    private List<ProjectOverview> searchByCategory(String category, String text){
        List<ProjectOverview> result = null;

        if(!isCategoryExist(category)){
            result = findAllProjectOverviews();
        }else if(category.equals(TITLE)){
            result = findProjectOverviewsByTitle(text);
        }else if(category.equals(CONTENT)){
            result = findProjectOverviewsByContent(text);
        }else if(category.equals(TITLE_AND_CONTENT)){
            List<ProjectOverview> resultByTitle = findProjectOverviewsByTitle(text);
            List<ProjectOverview> resultByContent = findProjectOverviewsByContent(text);
            result = concatResult(resultByTitle, resultByContent);
        }else if(category.equals(NICKNAME)){
            result = findProjectOverviewsByNickname(text);
        }else if(category.equals(TECHSTACK)) {
            result = findProjectOverviewsByTechstack(text);
        }

        return result;
    }

    private boolean isCategoryExist(String category){
        if(category == null || category.equals(NONE))
            return false;
        return true;
    }

    private List<ProjectOverview> findAllProjectOverviews(){
        return projectOverviewRepository.findAll();
    }

    private List<ProjectOverview> findProjectOverviewsByTitle(String text){
        return projectOverviewRepository.findByIncludeTitle(text);
    }

    private List<ProjectOverview> findProjectOverviewsByContent(String text){
        List<ProjectOverview> projects = null;
        List<ProjectContent> contentResult = projectContentRepository.findByIncludeContent(text);
        if(isContentExist(contentResult))
            projects = getProjectOverviewByContent(contentResult);
        return projects;
    }

    private boolean isContentExist(List<ProjectContent> contents){
        if(contents != null)
            return true;
        return false;
    }

    private List<ProjectOverview> getProjectOverviewByContent(List<ProjectContent> contents){
        List<ProjectOverview> projects = new ArrayList<ProjectOverview>();
        for(ProjectContent pc: contents)
            projects.add(pc.getProjectOverview());

        projects = removeDuplication(projects);
        return projects;
    }

    private List<ProjectOverview> removeDuplication(List<ProjectOverview> projects){
        HashSet<ProjectOverview> rmd = new HashSet<ProjectOverview>(projects);
        projects = new ArrayList<ProjectOverview>(rmd);
        return projects;
    }

    private List<ProjectOverview> concatResult(List<ProjectOverview> result1, List<ProjectOverview> result2){
        List<ProjectOverview> concatedResult = new ArrayList<ProjectOverview>(result1);
        concatedResult.removeAll(result2);
        concatedResult.addAll(result2);
        return concatedResult;
    }

    private List<ProjectOverview> findProjectOverviewsByNickname(String text) {
        return projectOverviewRepository.findByIncludeNickname(text);
    }

    private List<ProjectOverview> findProjectOverviewsByTechstack(String text) {
        return projectOverviewRepository.findByIncludeTechstack(text);
    }

    private void sortProjectOverviews(List<ProjectOverview> result, String orderby){
        if(isOrderbyExist(orderby) && isResultExist(result)){
            Comparator<ProjectOverview> comparator = getComparator(orderby);
            result.sort(comparator);
        }
    }

    private boolean isOrderbyExist(String orderby){
        if(orderby == null || orderby.equals("none"))
            return false;
        return true;
    }

    private boolean isResultExist(List<ProjectOverview> projects){
        if(projects == null)
            return false;
        return true;
    }

    private Comparator<ProjectOverview> getComparator(String orderby){
        if(orderby.equals(START_DATE))
            return projectOverviewComparator.orderByStartDate();
        else if(orderby.equals(END_DATE))
            return projectOverviewComparator.orderByEndDate();
        else if(orderby.equals(NICKNAME))
            return projectOverviewComparator.orderByNickname();
        else if(orderby.equals(TITLE))
            return projectOverviewComparator.orderByTitle();

        return null;
    }
}
