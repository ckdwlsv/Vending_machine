package vMachine_v3.view;

import vMachine_v3.dto.DrinkDto;

import java.util.List;

public class DrinkView {

    public void printMenuList(List<DrinkDto> list) {
        System.out.println("ID    제품명        가격      재고");
        System.out.println("---------------------------------");

        for (DrinkDto dto : list) {
            String text = dto.getId() + "   " + dto.getName() + "      "+ dto.getPrice()+"원     "+dto.getStock()+"개";
            if (dto.getStock() == 0) {
                text = text + " (품절)";
            }
            System.out.println(text);
        }
    }
}
