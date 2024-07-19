package com.example.chat.service

import com.example.chat.data.Room
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import com.example.chat.data.RoomWithUsers

@Service
class RoomService {

    private val logger = LoggerFactory.getLogger(RoomService::class.java)
    private val rooms = ConcurrentHashMap<String, Room>()
    private val roomUsers = ConcurrentHashMap<String, MutableList<String>>()

    // 모든 방 조회
    fun getAllRooms(): List<Room> {
        val allRooms = rooms.values.toList()
        logger.info("모든 방 목록 조회: ${allRooms.size}개")
        return allRooms
    }

    // 방 생성 후 로그 메시지 추가
    fun createRoom(name: String): Room {
        require(name.isNotBlank()) { "방 이름은 공백일 수 없습니다." }
        require(name.length in 3..20) { "방 이름은 3자 이상 20자 이하여야 합니다." }
        require(name.matches(Regex("^[a-zA-Z0-9_]+$"))) { "방 이름은 영문자, 숫자, 밑줄(_)만 사용할 수 있습니다." }

        if (rooms.containsKey(name)) {
            logger.error("방 이름 중복: $name")
            throw IllegalArgumentException("이미 존재하는 방 이름입니다.")
        }

        val room = Room(name = name)
        rooms[name] = room
        roomUsers[name] = mutableListOf()
        logger.info("방 생성: $room, 방 목록: $rooms")

        // 데이터 저장 확인 로그 추가
        logger.info("방 생성 후 rooms 맵: $rooms")
        logger.info("방 생성 후 roomUsers 맵: $roomUsers")

        return room
    }

    // 방 이름으로 방 조회
    fun getRoomByName(name: String): Room? {
        val room = rooms[name]
        if (room != null) {
            logger.info("방 조회 성공: $room")
        } else {
            logger.warn("방 조회 실패: $name")
            logger.warn("현재 방 목록: $rooms")
        }
        return room
    }
    // 방의 유저 목록 조회
    fun getUsersInRoom(roomName: String): List<String> {
        val users = roomUsers[roomName] ?: throw IllegalArgumentException("방을 찾을 수 없습니다")
        logger.info("유저 목록 조회 성공: $users")
        return users
    }

    // 방과 유저 목록 조회
    fun getRoomWithUsers(roomName: String): RoomWithUsers {
        val room = getRoomByName(roomName) ?: throw IllegalArgumentException("방을 찾을 수 없습니다")
        val users = roomUsers[roomName] ?: throw IllegalArgumentException("유저 목록을 찾을 수 없습니다")
        return RoomWithUsers(room, users)
    }

    // 방에 유저 추가
    fun addUserToRoom(roomName: String, username: String) {
        val room = rooms[roomName] ?: throw IllegalArgumentException("방을 찾을 수 없습니다")

        // 방에 유저 추가
        val users = roomUsers.computeIfAbsent(roomName) { mutableListOf() }
        users.add(username)

        logger.info("방에 유저 추가: 방=$roomName, 유저=$username")
        logger.info("방에 유저 추가 후 roomUsers 맵: $roomUsers")
    }

    // 방에서 유저 제거
    fun removeUserFromRoom(roomName: String, username: String) {
        val users = roomUsers[roomName] ?: throw IllegalArgumentException("방을 찾을 수 없습니다")

        // 방에서 유저 제거
        users.remove(username)

        logger.info("방에서 유저 제거: 방=$roomName, 유저=$username")
        logger.info("방에서 유저 제거 후 roomUsers 맵: $roomUsers")
    }
}
