package com.hope.projectrepository.domain.service.project;

import com.hope.projectrepository.domain.entity.FileInfo;
import com.hope.projectrepository.domain.entity.ProjectContent;
import com.hope.projectrepository.domain.entity.ProjectOverview;
import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.compatibility.dto.ContentDTO;
import com.hope.projectrepository.compatibility.dto.ContentResultDTO;
import com.hope.projectrepository.compatibility.dto.ProjectDTO;
import com.hope.projectrepository.domain.repository.FileInfoRepository;
import com.hope.projectrepository.domain.repository.ProjectContentRepository;
import com.hope.projectrepository.domain.repository.ProjectOverviewRepository;
import com.hope.projectrepository.util.comparator.ProjectContentComparator;
import com.hope.projectrepository.util.Result;
import com.hope.projectrepository.util.Util;
import com.hope.projectrepository.util.comparator.ProjectOverviewComparator;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ProjectService {
    @Autowired
    ProjectOverviewRepository projectOverviewRepository;

    @Autowired
    ProjectContentRepository projectContentRepository;

    @Autowired
    FileInfoRepository fileInfoRepository;

    @Autowired
    ProjectOverviewComparator projectOverviewComparator;

    @Value("${app.file.save_path}")
    String fileSavePath;

    private final String TITLE_AND_CONTENT="title_and_content";
    private final String TITLE="title";
    private final String CONTENT="content";
    private final String NICKNAME="nickname";
    private final String TECHSTACK="techstack";
    private final String START_DATE="project_start_date";
    private final String END_DATE="project_end_date";
    private final String CREATE_DATE="create_date";
    private final String UPDATE_DATE="update_date";

    public List<ProjectOverview> search(String category, String orderby, String text){
        List<ProjectOverview> result = null;
        List<ProjectContent> contentResult = null;

        if(category == null || category.equals("none")){
            result = projectOverviewRepository.findAll();
        }else{
            switch(category){
                case TITLE:
                    result = projectOverviewRepository.findByIncludeTitle(text);
                    break;
                case CONTENT:
                    contentResult = projectContentRepository.findByIncludeContent(text);

                    result = new ArrayList<ProjectOverview>();
                    for(ProjectContent pc: contentResult)
                        result.add(pc.getProjectOverview());

                    if(result != null){
                        HashSet<ProjectOverview> rmd = new HashSet<ProjectOverview>(result);
                        result = new ArrayList<ProjectOverview>(rmd);
                    }
                    break;
                case TITLE_AND_CONTENT:
                    result = projectOverviewRepository.findByIncludeTitle(text);
                    contentResult = projectContentRepository.findByIncludeContent(text);

                    for(ProjectContent pc: contentResult)
                        result.add(pc.getProjectOverview());

                    if(result != null){
                        HashSet<ProjectOverview> rmd = new HashSet<ProjectOverview>(result);
                        result = new ArrayList<ProjectOverview>(rmd);
                    }
                    break;
                case NICKNAME:
                    result = projectOverviewRepository.findByIncludeNickname(text);
                    break;
                case TECHSTACK:
                    result = projectOverviewRepository.findByIncludeTechstack(text);
                    break;
            }
        }

        if(orderby != null && !orderby.equals("none") && result != null){
            switch(orderby){
                case START_DATE:
                    result.sort(projectOverviewComparator.orderByStartDate());
                    break;
                case END_DATE:
                    result.sort(projectOverviewComparator.orderByEndDate());
                    break;
                case NICKNAME:
                    result.sort(projectOverviewComparator.orderByNickname());
                    break;
                case TITLE:
                    result.sort(projectOverviewComparator.orderByTitle());
                    break;
            }
        }

        return result;
    }

    public List<ContentResultDTO> getContentsInfo(ProjectOverview projectOverview){
        List<ContentResultDTO> contentsInfo = new ArrayList<>();

        List<ProjectContent> contents = projectContentRepository.findByProjectOverview(projectOverview);
        contents.sort(new ProjectContentComparator());

        for(ProjectContent content: contents){
            FileInfo file = fileInfoRepository.findByProjectContent(content);
            contentsInfo.add(new ContentResultDTO(content, file));
        }

        return contentsInfo;
    }

    @Transactional
    public ProjectOverview uploadProject(ProjectDTO projectDTO, MultipartFile[] files){
        User user = Util.getCurrentUser();

        String title = projectDTO.getTitle();
        String subject = projectDTO.getSubject();
        String scale = projectDTO.getScale();
        String techstack = projectDTO.getTechstack();
        String startDate =projectDTO.getStartDate();
        String endDate = projectDTO.getEndDate();

        ContentDTO[] contentDTOS = projectDTO.getContentDTOS();

        ProjectOverview newOverview = ProjectOverview.builder()
                .user(user)
                .mainTitle(title)
                .subject(subject)
                .scale(scale)
                .techStack(techstack)
                .projectStartDate(parseString(startDate))
                .projectEndDate(parseString(endDate))
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        projectOverviewRepository.save(newOverview);

        for(int i = 0; i < contentDTOS.length; i++){
            ProjectContent newContent = ProjectContent.builder()
                    .projectOverview(newOverview)
                    .content(contentDTOS[i].getContent().replace("\n","</br>"))
                    .subTitle(contentDTOS[i].getContentTitle())
                    .no(contentDTOS[i].getNo())
                    .build();
            projectContentRepository.save(newContent);
            if(!files[i].getOriginalFilename().equals("__dummy"))
                uploadFile(newContent,files[i]);

        }

        return newOverview;
    }

    @Transactional
    public String uploadFile(ProjectContent content, MultipartFile file){
        String rootPath = fileSavePath + content.getProjectOverview().getUser().getUserId().toString() + "/";
        String filePath = rootPath+ UUID.randomUUID();

        File target = new File(filePath);
        try{
//            동일파일 존재시, 처리해주는 코드
            InputStream fileStream = file.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream,target);
        }catch (Exception e){
            FileUtils.deleteQuietly(target);
            e.printStackTrace();
        }

        FileInfo fileInfo = FileInfo.builder()
                .projectContent(content)
                .filePath(filePath)
                .fileName(file.getOriginalFilename())
                .build();

        fileInfoRepository.save(fileInfo);

        return Result.SUCCESS;
    }

    public LocalDate parseString(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date,formatter);
    }
}
