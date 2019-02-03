package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );

        List<UserMealWithExceed> list=getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
//        .toLocalDate();
//        .toLocalTime();
        for(UserMealWithExceed u:list) System.out.println(u.toString());

    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> mealListWithExceed =new ArrayList<>();
        Map<LocalDate,Integer> dateTimeMap = new HashMap<>();
        List<UserMealWithExceed> list=new ArrayList<>();
        for(UserMeal userMealAtDay:mealList)
        {
            if(dateTimeMap.isEmpty()) dateTimeMap.put(userMealAtDay.getDateTime().toLocalDate(),userMealAtDay.getCalories());
            else {
                boolean t = false;
                for (Map.Entry<LocalDate, Integer> localDate : dateTimeMap.entrySet()) {
                    if (localDate.getKey().equals(userMealAtDay.getDateTime().toLocalDate())) {
                        dateTimeMap.put(localDate.getKey(), userMealAtDay.getCalories() + localDate.getValue());
                        t=true;
                        break;
                    }

                }
                if (!t) dateTimeMap.put(userMealAtDay.getDateTime().toLocalDate(), userMealAtDay.getCalories());
            }
        }

        for(UserMeal mealWithExceed:mealList){
            boolean t=false;
            for(Map.Entry<LocalDate,Integer> map:dateTimeMap.entrySet())
            {
                if(map.getKey().equals(mealWithExceed.getDateTime().toLocalDate())){
                    t=(map.getValue()>caloriesPerDay);
                    break;
                }

            }
            mealListWithExceed.add(new UserMealWithExceed(mealWithExceed.getDateTime(),mealWithExceed.getDescription(),mealWithExceed.getCalories(),t));
        }
        for (UserMealWithExceed meal:mealListWithExceed)
        {
            if(TimeUtil.isBetween(meal.getDateTime().toLocalTime(),startTime,endTime))
                list.add(meal);
        }

        return list;
    }
}
