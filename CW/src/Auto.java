abstract class Auto implements Comparable<Auto> {
    class TypeException extends Exception {
        TypeException(String type) {
            super("\'" + type + "\' is not a valid auto type!");
        }
    }
    private String name;
    private String color;
    protected enum Type{OIL, DIESEL};
    private Type type;

    public Auto(String name, String color, Type type) {
        this.name = name;
        this.color = color;
        this.type = type;
    }
    public Auto(String name, String color, String sType) throws TypeException {
        Type type;
        switch (sType) {
            case "oil": type = Type.OIL; break;
            case "diesel": type = Type.DIESEL; break;
            default: throw new TypeException(sType);
        }
        this.name = name;
        this.color = color;
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public String getColor() {
        return color;
    }
    public Type getType() {
        return type;
    }

    @Override
    public int compareTo(Auto o) {
        return (!getName().equals(o.getName()))? getName().compareTo(o.getName()):
                (!getType().equals(o.getType()))? o.getType().compareTo(getType()):
                        getColor().compareTo(o.getColor());
    }
    @Override
    public String toString() {
        return "Name: " + getName() + ", type: " + ((getType() == Type.OIL)? "oil": "diesel") + ", color: " + getColor();
    }
}

class Car extends Auto {
    private enum Material{SKIN, POLYESTER};
    private Material material;
    class MaterialException extends Exception {
        MaterialException(String material) {
            super("\'" + material + "\' is not a valid seat material!");
        }
    }

    public Car(String name, String color, Auto.Type type, Material material) {
        super(name, color, type);
        this.material = material;
    }
    public Car(String name, String color, String sType, String sMaterial) throws TypeException, MaterialException {
        super(name, color, sType);
        Material material;
        switch (sMaterial) {
            case "skin": material = Material.SKIN; break;
            case "polyester": material = Material.POLYESTER; break;
            default: throw new MaterialException(sMaterial);
        }
        this.material = material;
    }
    public Material getMaterial() {
        return material;
    }

    @Override
    public int compareTo(Auto o) {
        int cmp = super.compareTo(o);
        return (cmp == 0)? getMaterial().compareTo(((Car)o).getMaterial()): cmp;
    }
    @Override
    public String toString() {
        return super.toString() + ", material: " + ((getMaterial() == Material.SKIN)? "skin": "polyester");
    }
}
class Bus extends Auto {
    private int numSeats;
    private int numDoors;
    class SeatsException extends Exception {
        SeatsException(int seats) {
            super("\'" + seats + "\' is not a valid number of seats!");
        }
    }
    class DoorsException extends Exception {
        DoorsException(int doors) {
            super("\'" + doors + "\' is not a valid number of doors!");
        }
    }

    public Bus(String name, String color, Type type, int numSeats, int numDoors) {
        super(name, color, type);
        this.numSeats = numSeats;
        this.numDoors = numDoors;
    }
    public Bus(String name, String color, String sType, int numSeats, int numDoors) throws TypeException, DoorsException, SeatsException {
        super(name, color, sType);
        if (numDoors < 1) throw new DoorsException(numDoors);
        this.numDoors = numDoors;
        if (numSeats < 1) throw new SeatsException(numSeats);
        this.numSeats = numSeats;
    }

    public int getNumSeats() {
        return numSeats;
    }
    public int getNumDoors() {
        return numDoors;
    }

    @Override
    public int compareTo(Auto o) {
        int cmp = super.compareTo(o);
        return (cmp == 0)?
                ((getNumSeats() == ((Bus)o).getNumSeats())?
                getNumDoors() - ((Bus)o).getNumDoors() : (getNumSeats() - ((Bus)o).getNumSeats())):
                cmp;
    }
    @Override
    public String toString() {
        return super.toString() + ", seats number: " + getNumSeats() + ", doors number: " + getNumDoors();
    }
}