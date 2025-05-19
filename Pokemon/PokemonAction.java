package Projek_BIBD_Pokemon.Pokemon;

import java.util.Map;
import java.util.HashMap;

public class PokemonAction {
    protected String actionName;
    protected PokemonActionType actionType;
    protected int sp;
    protected String desc;

    public PokemonAction(String actionName, PokemonActionType actionType, int sp, String desc) {
        this.actionName = actionName;
        this.actionType = actionType;
        this.sp = sp;
        this.desc = desc;
    }

    public String getActionName() {
        return actionName;
    }
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public PokemonActionType getActionType() {
        return actionType;
    }
    public void setActionType(PokemonActionType actionType) {
        this.actionType = actionType;
    }

    public int getSp() {
        return sp;
    }
    public void setSp(int sp) {
        this.sp = sp;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public boolean isSpEnough() {
        return sp > 0;
    }

    public void useSp() {
        sp--;
    }

    public Map<String, String> useActionInfo() {
        if (!isSpEnough()) {
            System.out.println("SP not enough");
            return null;
        } else {
            Map<String, String> map = new HashMap<String, String>();
            map.put("actionName", actionName);
            map.put("actionType", actionType.toString());
            map.put("sp", String.valueOf(sp));
            map.put("desc", desc);
            return map;
        }
    }

    public String toString() {
        return actionName + " " + actionType + " " + sp + " " + desc;
    }
}
