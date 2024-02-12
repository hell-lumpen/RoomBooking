package org.mai.roombooking.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    /**
     * Метод сохранения/обновления данных учебной группы
     * @param group учебная группа
     */
    public void saveGroup(@NonNull Group group) {
        var existingGroup = groupRepository.findByName(group.getName());
        if (existingGroup.isEmpty()) {
            groupRepository.save(group);
        }
        else {
            group.setId(existingGroup.get().getId());
            groupRepository.save(group);
        }
    }

    public void saveGroups(@NonNull List<Group> groups) {
        for (var group : groups)
            this.saveGroup(group);
    }
}
