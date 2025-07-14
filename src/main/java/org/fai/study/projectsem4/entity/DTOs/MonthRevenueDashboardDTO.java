package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthRevenueDashboardDTO {
    private String month;
    private Integer totalOrder;
    private Double totalRevenue;
}
