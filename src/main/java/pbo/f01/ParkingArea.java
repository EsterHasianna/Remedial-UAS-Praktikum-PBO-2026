package pbo.f01;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class untuk area parkir
 * Nama: Ester Hasianna Nainggolan
 * Nim: 12S24050
 */
@Entity
@Table(name = "parking_area")
public class ParkingArea {

    @Id
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "allowed_type", nullable = false)
    private String allowedType;

    @OneToMany(mappedBy = "parkingArea", fetch = FetchType.EAGER)
    @OrderBy("plateNumber ASC")
    private List<Vehicle> vehicles = new ArrayList<>();

    public ParkingArea() {}

    public ParkingArea(String name, int capacity, String allowedType) {
        this.name = name;
        this.capacity = capacity;
        this.allowedType = allowedType;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getAllowedType() { return allowedType; }
    public void setAllowedType(String allowedType) { this.allowedType = allowedType; }

    public List<Vehicle> getVehicles() { return vehicles; }
    public void setVehicles(List<Vehicle> vehicles) { this.vehicles = vehicles; }

    
}
