package qqhl.andaalarmmaster.service.deviceuserbinding;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import qqhl.andaalarmmaster.service.device.Device;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@Data
public class DeviceUserBinding {
    public static final DeviceUserBinding Null = new DeviceUserBinding();
    @JsonIgnore
    @EmbeddedId
    private Device.PK pk;
    private String userId;
    private Date addTime;

    public String getDeviceId() {
        return pk == null ? null : pk.getDeviceId();
    }

    public String getDeviceType() {
        return pk == null ? null : pk.getDeviceType();
    }
}
