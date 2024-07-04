export interface Need{
    /*@JsonProperty("name") private String name;
    @JsonProperty("id") private int id;
    @JsonProperty("goalMet") private boolean metGoal;
    @JsonProperty("cost") private int cost;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("type") private String type;*/

    name: string;
    id: number;
    metGoal: boolean;
    cost: number;
    quantity: number;
    type: string;
    surplus: number;

}