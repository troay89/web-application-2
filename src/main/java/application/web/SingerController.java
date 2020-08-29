package application.web;

import application.entities.Singer;
import application.services.SingerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/singers")
public class SingerController {

    private static final Logger log = LoggerFactory.getLogger(SingerController.class);

    private SingerService singerService;

    private MessageSource messageSource;

    @Autowired
    public void setSingerService(SingerService singerService) {
        this.singerService = singerService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model uiModel){
        log.info("listing singers");
        List<Singer> singers = singerService.findAll();
        singers.forEach(System.out::println);
        uiModel.addAttribute("singers" , singers);
        log.info("No. of singers: "+ singers.size());
        return "singers/list";
    }
}
