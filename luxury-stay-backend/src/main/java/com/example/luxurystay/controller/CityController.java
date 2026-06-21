package com.example.luxurystay.controller;

import com.example.luxurystay.dto.response.CityResponse;
import com.example.luxurystay.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/destinations")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;
    
    private static final int PAGE_SIZE = 12;

    @GetMapping
    public String getDestinations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Boolean hot,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "name"));
        Page<CityResponse> cityPage;
        long totalElements;
        
        if (hot != null && hot) {
            cityPage = cityService.getHotCitiesPaged(pageable);
            totalElements = cityService.countHotCities();
            model.addAttribute("filterType", "hot");
        } else if (region != null && !region.isEmpty()) {
            cityPage = cityService.getCitiesByRegionPaged(region, pageable);
            totalElements = cityService.countCitiesByRegion(region);
            model.addAttribute("filterType", "region");
            model.addAttribute("selectedRegion", region);
        } else {
            cityPage = cityService.getAllCitiesPaged(pageable);
            totalElements = cityService.countAllCities();
            model.addAttribute("filterType", "all");
        }
        
        model.addAttribute("cities", cityPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", cityPage.getTotalPages());
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("hasNext", cityPage.hasNext());
        model.addAttribute("hasPrevious", cityPage.hasPrevious());
        
        return "destinations";
    }

    @GetMapping("/hot")
    public String getHotCities(@RequestParam(defaultValue = "0") int page, Model model) {
        return getDestinations(page, null, true, model);
    }

    @GetMapping("/region/{region}")
    public String getCitiesByRegion(@PathVariable String region, @RequestParam(defaultValue = "0") int page, Model model) {
        return getDestinations(page, region, null, model);
    }

    @GetMapping("/{id}")
    public String getCityDetail(@PathVariable Long id, Model model) {
        CityResponse city = cityService.getCityById(id);
        if (city == null) {
            return "redirect:/destinations";
        }
        model.addAttribute("city", city);
        return "redirect:/search?location=" + city.getName();
    }
}
