package com.nextinnovation.pitak.model.car;

public class Car  implements Comparable< Car >{
    private Long id;
    private String name;

    public Car(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Car o) {
        return this.getId().compareTo(o.getId());
    }
}
