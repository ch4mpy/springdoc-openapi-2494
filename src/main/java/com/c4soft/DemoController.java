package com.c4soft;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    
    @GetMapping("/demo")
    public Dto getDemo(Dto dto) {
        return dto;
    }

    
    public static enum EnumSerializedByName {
        ON("is on"),
        OFF("is off");
        
        String label;
        
        EnumSerializedByName(String label) {
            this.label = label;
        }
        
        @Override
        public String toString() {
            return label;
        }
    }
    
    public static record Dto(EnumSerializedByName status) {
        
    }
}
