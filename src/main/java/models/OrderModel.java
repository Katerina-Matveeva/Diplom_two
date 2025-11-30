package models;


import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

@Getter
@Setter


public class OrderModel {
    public ArrayList<String> ingredients;

   public OrderModel(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
   }
}
