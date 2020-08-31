package com.cdroho.logicalrepository;

import com.cdroho.modle.ConsultationRoom;
import com.cdroho.modle.QueuingMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

/**
 * 诊室dao
 * JpaRepository必须指定一个类型，该类型必须是@Entity标注
 * @author HZL
 * @date 2020-3-31
 */
@Repository
public interface RoomRepository extends JpaRepository<ConsultationRoom,Integer> {

    /**
     * 删除诊室
     * @param roomId
     */
    @Transactional
    @Modifying
    @Query(value = "delete from room where id=?",nativeQuery = true)
    void deleteConsultationRoomById(Long roomId);


    /**
     * jpa----->设置map自动转换成查询字段为key的map
     * jpa----->设置list自动转换成值为查询结果值的数组
     * @return
     */
    @Query(value = "select m.id,m.room_name as roomName,m.room_ip as roomIp," +
            "m.room_profile as roomProfile,n.`name` as nurseName " +
            "from room m left join nursetriage n on n.id=m.nurse_id",
            nativeQuery = true)
    List<Object> queryAll();

    /**
     * 更新诊室
     * @param roomName
     * @param roomIp
     * @param roomProfile
     * @param triageId
     * @param roomId
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE room SET room_name = ?,room_ip = ?,room_profile = ?" +
            ",nurse_id=? WHERE id = ?",nativeQuery = true)
    void updateRoom(String roomName,String roomIp,String roomProfile,long triageId,Long roomId);


    /**
     * 新增诊室
     * @param roomName
     * @param roomIp
     * @param roomProfile
     * @param triageId
     * 如果多一个?占位符，少传一个参数，则报参数数目错误
     */
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO room (room_name, room_ip,room_profile,nurse_id) " +
            "VALUES(?,?,?,?)",nativeQuery = true)
    void insertRoom(String roomName,String roomIp,String roomProfile,long triageId);


    /**
     * 返回诊室数据
     * @param roomId
     * @return
     */
    @Query(value = "SELECT * FROM room WHERE id=?", nativeQuery = true)
    ConsultationRoom findConsultationRoomById(Long roomId);

    /**
     * 返回一条记录
     * @param name
     * @return
     */
    @Query(value = "SELECT * FROM room WHERE room_name=?", nativeQuery = true)
    ConsultationRoom findByName(String name);

    /**
     * 返回一条记录
     * @param ip
     * @return
     */
    @Query(value = "SELECT * FROM room WHERE room_ip=?", nativeQuery = true)
    ConsultationRoom findByIp(String ip);


    /**
     * 查询对应分诊台下的诊室，以及每个诊室的患者数量
     * @param nurseId
     * @return
     */
    @Query(value = "SELECT b.id AS queueId,b.queue_name AS queue_name," +
            "(SELECT COUNT(*) FROM sickqueue z WHERE z.sick_state IN (0,1,2,5) AND z.queue_id = b.id AND z.is_report = 2) AS wait," +
            "(SELECT COUNT(*) FROM sickqueue z WHERE sick_state IN (8, 9) AND z.queue_id = b.id) AS hascall " +
            "FROM nursetriage a INNER JOIN queuelist b ON a.id = b.nurse_id LEFT JOIN sickqueue c ON c.queue_id = b.id " +
            "WHERE a.id= ? GROUP BY b.id ORDER BY wait DESC",nativeQuery = true)
    List<Object> findRoomByNurseId(long nurseId);

    /*@Query(value = "select q.id as queueId,q.queue_name from nursetriage n inner join queuelist q on n.id=q.nurse_id where n.id=?",nativeQuery = true)
    List<Object> findRoomByNurseId(long nurseId);*/


    @Query(value = "SELECT c.sick_number AS queueId,b.queue_name AS queue_name," +
            "(SELECT COUNT(*) FROM sickqueue z WHERE z.sick_state IN (0,1,2,5) AND z.queue_id = b.id AND z.is_report = 2) AS wait," +
            "(SELECT COUNT(*) FROM sickqueue z WHERE sick_state IN (8, 9) AND z.queue_id = b.id) AS hascall,a.name as nurseName," +
            "(SELECT sick_number FROM sickqueue z WHERE sick_state IN (8) AND z.queue_id = b.id) AS jzxh," +
            "m.machine_name as zsmc,d.doctor_name as doctorName,c.sick_name as name FROM nursetriage a INNER JOIN queuelist b ON a.id = b.nurse_id " +
            "LEFT JOIN sickqueue c ON c.queue_id = b.id LEFT JOIN machine m ON c.doctor_id=m.doctor_id " +
            "LEFT JOIN doctor d ON m.doctor_id=d.id WHERE c.sick_code=? GROUP BY b.id ORDER BY wait DESC",nativeQuery = true)
    List<Object> findKsData(String sickCode);


    /**
     *查询与叫号器绑定的屏幕
     * @param machineId
     * @return
     */
    @Query(value = "select r.id,r.room_ip,r.room_name,r.room_profile,n.name from room r left join machine_room mc on r.id=mc.room_id left join machine m on mc.machine_id=m.id " +
            "left join nursetriage n on r.nurse_id=n.id where m.id=?", nativeQuery = true)
    List<Object> findRoomWithMachine(long machineId);

    /**
     *查询与叫号器绑定的屏幕
     * @param ip
     * @return
     */
    @Query(value = "select r.id,r.room_ip,r.room_name,r.room_profile,n.name from room r left join machine_room mc on r.id=mc.room_id left join machine m on mc.machine_id=m.id " +
            "left join nursetriage n on r.nurse_id=n.id where m.machine_ip=?", nativeQuery = true)
    List<Object> findRoomWithMachineIp(String ip);

    /**
     * 查询当前分诊台下绑定的屏幕，以及不在当前分诊台下绑定的屏幕
     * @param nurseId
     * @return
     */
    @Query(value = "select r.id,r.room_ip,r.room_name,r.room_profile,n.name from room r left join nursetriage n on r.nurse_id=n.id where n.id=? or r.id in (17,26,27,28,29)", nativeQuery = true)
    List<Object> findTriageRoom(long nurseId);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "INSERT INTO machine_room (machine_id, room_id) " +
            "VALUES(?,?)",nativeQuery = true)
    void insertRoomWithPager(long machineId,long roomId);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "delete from machine_room where  machine_id=? and room_id=?",nativeQuery = true)
    void deleteMachineRoom(long machineId,long roomId);
}
