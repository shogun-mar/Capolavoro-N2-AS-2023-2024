package component;

public class Cell {
    public int id;
    public boolean player = false, obs_bush = false, obs_crate =false, bomb = false;
    public boolean wall = false;

    public Cell(int id){
        this.id = id;
    }

    public Cell(Cell c){
        this.id = c.id;
        this.player = c.player;
        this.obs_bush = c.obs_bush;
        this.obs_crate = c.obs_crate;
        this.bomb = c.bomb;
        this.wall = c.wall;
    }

    public int getId() {
        return id;
    }

    public void setPlayer(int id){
        this.id = id;
        player = true;
        wall = false;
        obs_bush = false;
        obs_crate = false;
        bomb = false;
    }

    public void setWall(){
        player = false;
        wall = true;
        obs_bush = false;
        obs_crate = false;
        bomb = false;
        id=-1;
    }

    public void setBush(){
        player = false;
        wall = false;
        obs_bush = true;
        obs_crate = false;
        bomb = false;
        id=-1;
    }

    public void setCrate(){
        player = false;
        wall = false;
        obs_bush = false;
        obs_crate = true;
        bomb = false;
        id=-1;
    }

    public boolean isObs() {
        return obs_crate || obs_bush;
    }

    public void setBomb(){
        player = false;
        wall = false;
        obs_bush = false;
        obs_crate = false;
        bomb = true;
        id=-1;
    }

    public void setEmpty(){
        player = false;
        wall = false;
        obs_bush = false;
        obs_crate= false;
        bomb = false;
        id=-1;
    }

    public boolean isEmpty(){
        return !player && !wall && !obs_bush && !obs_crate && !bomb;
    }

    @Override
    public String toString() {
        if(bomb) return "B";
        if(player) return "P";
        if(wall) return "W";
        if(obs_bush) return "obj";
        if(obs_crate) return "C";
        return " ";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cell cell = (Cell) obj;
        return id == cell.id && player == cell.player && obs_bush == cell.obs_bush && obs_crate == cell.obs_crate && bomb == cell.bomb && wall == cell.wall;
    }
}
