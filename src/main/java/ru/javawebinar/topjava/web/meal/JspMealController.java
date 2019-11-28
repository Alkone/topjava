package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;


@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/filter")
    public String getFiltered(@RequestParam("startDate") String startDate,
                              @RequestParam("endDate") String endDate,
                              @RequestParam("startTime") String startTime,
                              @RequestParam("endTime") String endTime,
                              Model model) {
        LocalDate startLocalDate = parseLocalDate(startDate);
        LocalDate endLocalDate = parseLocalDate(endDate);
        LocalTime startLocalTime = parseLocalTime(startTime);
        LocalTime endLocalTime = parseLocalTime(endTime);

        model.addAttribute("meals", super.getBetween(startLocalDate, startLocalTime, endLocalDate, endLocalTime));
        return "meals";
    }

    @GetMapping("/create")
    public String getEmptyMealForm(Model model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/{id}/delete")
    public String deleteMeal(@PathVariable("id") int id) {
        super.delete(id);
        return "redirect:/meals";
    }

    @GetMapping("/{id}/update")
    public String getUpdatableMealForm(@PathVariable("id") int id, Model model) {
        model.addAttribute("meal", super.get(id));
        return "mealForm";
    }

    @PostMapping("/update")
    public String updateUser(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        super.update(meal, getId(request));
        return "redirect:/meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

}
