package pbo.f01;

import jakarta.persistence.*;
import java.util.*;

/**
 * Driver class utama
 * Nama: Ester Hasianna Nainggolan
 * Nim: 12S24050
 */
public class App {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("parkit-pu");
        EntityManager em = emf.createEntityManager();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split("#");
            String command = parts[0];

            switch (command) {

                case "area-add": {
                    if (parts.length < 4) break;
                    String name        = parts[1];
                    int    capacity    = Integer.parseInt(parts[2]);
                    String allowedType = parts[3];

                    em.getTransaction().begin();
                    ParkingArea existing = em.find(ParkingArea.class, name);
                    if (existing == null) {
                        em.persist(new ParkingArea(name, capacity, allowedType));
                    }
                    em.getTransaction().commit();
                    break;
                }

                case "vehicle-add": {
                    if (parts.length < 4) break;
                    String plate = parts[1];
                    String owner = parts[2];
                    String type  = parts[3];

                    em.getTransaction().begin();
                    Vehicle existing = em.find(Vehicle.class, plate);
                    if (existing == null) {
                        em.persist(new Vehicle(plate, owner, type));
                    }
                    em.getTransaction().commit();
                    break;
                }

                case "park": {
                    if (parts.length < 3) break;
                    String plate    = parts[1];
                    String areaName = parts[2];

                    em.getTransaction().begin();
                    Vehicle     vehicle = em.find(Vehicle.class, plate);
                    ParkingArea area    = em.find(ParkingArea.class, areaName);

                    if (vehicle != null && area != null) {
                        if (vehicle.getType().equals(area.getAllowedType())) {
                            long occupancy = countOccupancy(em, area.getName());
                            if (occupancy < area.getCapacity()) {
                                vehicle.setParkingArea(area);
                                em.merge(vehicle);
                            }
                        }
                    }
                    em.getTransaction().commit();
                    break;
                }

                case "display-all": {
                    List<ParkingArea> areas = em.createQuery(
                            "SELECT a FROM ParkingArea a ORDER BY a.name ASC",
                            ParkingArea.class)
                        .getResultList();

                    for (ParkingArea area : areas) {
                        long occupancy = countOccupancy(em, area.getName());
                        System.out.println(area.getName() + " " + area.getAllowedType()
                                + " " + area.getCapacity() + "|" + occupancy);

                        List<Vehicle> vehicles = em.createQuery(
                                "SELECT v FROM Vehicle v WHERE v.parkingArea.name = :aname ORDER BY v.plateNumber ASC",
                                Vehicle.class)
                            .setParameter("aname", area.getName())
                            .getResultList();

                        for (Vehicle v : vehicles) {
                            System.out.println(v.getPlateNumber() + " " + v.getOwner()
                                    + " " + v.getType());
                        }
                    }
                    break;
                }

                default:
                    break;
            }
        }

        em.close();
        emf.close();
    }

    private static long countOccupancy(EntityManager em, String areaName) {
        return em.createQuery(
                "SELECT COUNT(v) FROM Vehicle v WHERE v.parkingArea.name = :aname",
                Long.class)
            .setParameter("aname", areaName)
            .getSingleResult();
    }
}