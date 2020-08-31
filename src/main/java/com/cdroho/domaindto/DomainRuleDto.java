package com.cdroho.domaindto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 规则
 * @author HZL
 */
public class DomainRuleDto {

    private int return_flag_step;

    private int late_flag_step;

    public void setReturn_flag_step(int return_flag_step) {
        this.return_flag_step = return_flag_step;
    }

    public void setLate_flag_step(int late_flag_step) {
        this.late_flag_step = late_flag_step;
    }

    public int getReturn_flag_step() {
        return return_flag_step;
    }

    public int getLate_flag_step() {
        return late_flag_step;
    }

}
