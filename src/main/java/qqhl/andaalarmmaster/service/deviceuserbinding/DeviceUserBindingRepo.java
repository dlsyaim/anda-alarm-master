package qqhl.andaalarmmaster.service.deviceuserbinding;

import org.springframework.data.repository.CrudRepository;
import qqhl.andaalarmmaster.service.device.Device;

import java.util.Optional;

public interface DeviceUserBindingRepo extends CrudRepository<DeviceUserBinding, Device.PK> {
    Iterable<DeviceUserBinding> findByUserId(String userId);
    Optional<DeviceUserBinding> findByPkDeviceIdAndPkDeviceType(String deviceId, String deviceType);
    Optional<DeviceUserBinding> findByUserIdAndPkDeviceType(String userId, String deviceType);
}
