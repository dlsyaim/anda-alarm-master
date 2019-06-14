package qqhl.andaalarmmaster.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name="anda_tel_alarm_notice_phone")
@Entity
@Data
public class AlarmNoticePhone {
    @JsonIgnore
    @EmbeddedId
    private AlarmNoticePhone.PK pk;
    private String memo;

    @Data
    public static class PK implements Serializable {
        private String hostId;
        private String phone;
    }

    public String getHostId() { return pk.hostId; }
    public String getPhone() { return pk.phone; }
}
