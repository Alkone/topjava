package ru.javawebinar.topjava.web;


import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class MealServlet extends HttpServlet {
    private DateTimeFormatter dateTimeFormatter;

    private MealDao mealDao;

    @Override
    public void init() {
        dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        mealDao = new MealDaoMemory();
        //Add simple data
        MealsUtil.getSimpleData().forEach(meal -> mealDao.add(meal));
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                case "edit": {
                    request.setAttribute("meal", mealDao.get(Long.parseLong(request.getParameter("id"))));
                    break;
                }
                case "delete": {
                    mealDao.delete(Long.parseLong(request.getParameter("id")));
                    response.sendRedirect(request.getContextPath() + "/meals");
                    return;
                }
            }
        }
        request.setAttribute("dateTimeFormatter", dateTimeFormatter);
        request.setAttribute("meals", MealsUtil.getFiltered(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            mealDao.add(new Meal(dateTime, description, calories));
        } else {
            mealDao.update(new Meal(Long.parseLong(id), dateTime, description, calories));
        }
        response.sendRedirect(request.getContextPath() + "/meals");
    }
}
