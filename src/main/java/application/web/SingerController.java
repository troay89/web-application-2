package application.web;

import application.entities.Singer;
import application.services.SingerService;
import application.util.Message;
import application.util.SingerGrid;
import application.util.UrlUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model uiModel) {
        log.info("listing singers");
        List<Singer> singers = singerService.findAll();
        uiModel.addAttribute("singers", singers);
        log.info("No. of singers: " + singers.size());
        return "singers/list";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        log.info("просмотр певца по id {}", id);
        Singer singer = singerService.findById(id);
        uiModel.addAttribute("singer", singer);
        log.info("певец найден");
        return "singers/show";
    }


    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
    public String update(@Valid Singer singer, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest,
                         RedirectAttributes redirectAttributes, Locale locale) {
        log.info("Updating singer");
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", new Message("error", messageSource.getMessage("singer_save_fail",
                    new Object[]{}, locale)));
            uiModel.addAttribute("singer", singer);
            return "singers/update";
        }
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message", new Message("success", messageSource.getMessage("singer_save_success",
                new Object[]{}, locale)));
        singerService.save(singer);
        log.info("Singer id {} update", singer.getId());
        return "redirect:/singers/" + UrlUtil.encodeUrlPathSegment(singer.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("singer", singerService.findById(id));
        return "singers/update";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String save(@Valid Singer singer, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest,
                       RedirectAttributes redirectAttributes, Locale locale) {
        log.info("new singer {}", singer.getId());
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", new Message("error", messageSource.getMessage("singer_save_fail",
                    new Object[]{}, locale)));
            uiModel.addAttribute("singer", singer);
            return "singers/create";
        }
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message", new Message("success", messageSource.getMessage("singer_save_success",
                new Object[]{}, locale)));
        singerService.save(singer);
        log.info("Singer id {} save", singer.getId());
        return "redirect:/singers/";
    }

    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        Singer singer = new Singer();
        uiModel.addAttribute("singer", singer);
        return "singers/create";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id) {
        Singer singer = singerService.findById(id);
        singerService.delete(singer);
        return "redirect:/singers";
    }



    @ResponseBody
    @RequestMapping(value = "/listgrid", method = RequestMethod.GET, produces="application/json")
    public SingerGrid listGrid(@RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "rows", required = false) Integer rows,
                               @RequestParam(value = "sidx", required = false) String sortBy,
                               @RequestParam(value = "sord", required = false) String order) {

        log.info("Listing singers for grid with page: {}, rows: {}", page, rows);
        log.info("Listing singers for grid with sort: {}, order: {}", sortBy, order);

        // Process order by
        Sort sort = null;
        String orderBy = sortBy;
        if (orderBy != null && orderBy.equals("birthDateString"))
            orderBy = "birthDate";

        if (orderBy != null && order != null) {
            if (order.equals("desc")) {
                sort = Sort.by(Sort.Direction.DESC, orderBy);
            } else
                sort = Sort.by(Sort.Direction.ASC, orderBy);
        }


        PageRequest pageRequest = null;

        if (sort != null) {
            pageRequest =  PageRequest.of(page - 1, rows, sort);
        } else {
            pageRequest = PageRequest.of(page - 1, rows);
        }

        Page<Singer> singerPage = singerService.findAllByPage(pageRequest);

        // Construct the grid data that will return as JSON data
        SingerGrid singerGrid = new SingerGrid();

//        начальная страница
        singerGrid.setCurrentPage(singerPage.getNumber() + 1);
//        количество станиц
        singerGrid.setTotalPages(singerPage.getTotalPages());
//        всего объектов
        singerGrid.setTotalRecords(singerPage.getTotalElements());
//        объекты
        singerGrid.setSingerData(Lists.newArrayList(singerPage.iterator()));

        return singerGrid;
    }
}
