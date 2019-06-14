package qqhl.andaalarmmaster.service;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AlarmNoticePhoneRepo extends CrudRepository<AlarmNoticePhone, AlarmNoticePhone.PK> {
    List<AlarmNoticePhone> findByPkHostId(String userId);
}
