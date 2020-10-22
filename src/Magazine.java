public class Magazine {
    private int capacity, ammo;

    public Magazine() {
        this.capacity = 10;
        this.ammo = 10;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }
    public void increaseCapacity(int amount) {
        capacity += amount;
    }
    public void decreaseAmmo() {
        ammo--;
    }
}
