package com.teamtreehouse.core;

import com.teamtreehouse.control.Control;
import com.teamtreehouse.control.ControlRepository;
import com.teamtreehouse.device.Device;
import com.teamtreehouse.device.DeviceRepository;
import com.teamtreehouse.room.Room;
import com.teamtreehouse.room.RoomRepository;
import com.teamtreehouse.user.User;
import com.teamtreehouse.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class DatabaseLoader implements ApplicationRunner {
    private final RoomRepository rooms;
    private final DeviceRepository devices;
    private final ControlRepository controls;
    private final UserRepository users;

    @Autowired
    public DatabaseLoader(RoomRepository rooms, DeviceRepository devices,
                          ControlRepository controls, UserRepository users) {
        this.rooms = rooms;
        this.devices = devices;
        this.controls = controls;
        this.users = users;

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<User> userList = Arrays.asList(
                new User("User1", new String[] {"ROLE_USER"}, "User1"),
                new User("User2", new String[] {"ROLE_USER"}, "User2"),
                new User("Admin", new String[] {"ROLE_USER", "ROLE_ADMIN"}, "Admin")

        );
        users.save(userList);

        List<Room> roomList = new ArrayList<>();
        List<Control> controlList = new ArrayList<>();
        List<Device> deviceList = new ArrayList<>();

        IntStream.range(0, 100)
                .forEach(i -> {
                    String roomName = String.format("Room %d", i + 1);
                    Room room = new Room(roomName, 1 + (int)(Math.random() * 1000));

                    String deviceName = String.format("Device %d", i + 1);
                    Device device = new Device(deviceName);

                    String controlName = String.format("Control %d", i + 1);
                    Control control = new Control(controlName);

                    controls.save(controlList);
                    device.addControl(control);
                    room.addDevice(device);
                    devices.save(deviceList);
                    int index = (int)(Math.random() * userList.size());
                    List<User> tempUsers = new ArrayList<>();
                    tempUsers.add(userList.get(index));
                    room.setAdministrators(tempUsers);
                    roomList.add(room);
                });
        rooms.save(roomList);
    }
}
