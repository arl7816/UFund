import {Need} from './Need';

export class FundingBasket{
    /*@JsonProperty("name") private String name;
    @JsonProperty("id") private int id;
    @JsonProperty("goalMet") private boolean metGoal;
    @JsonProperty("cost") private int cost;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("type") private String type;*/

    needs: Need[];

    constructor(){
        this.needs = new Array<Need>;

    }

}