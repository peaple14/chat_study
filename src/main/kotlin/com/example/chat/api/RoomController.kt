package com.example.chat.api

import com.example.chat.data.Room
import com.example.chat.data.RoomWithUsers
import com.example.chat.service.RoomService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/")
class RoomController(private val roomService: RoomService) {

    private val logger = LoggerFactory.getLogger(RoomController::class.java)

    @GetMapping("/room_list")
    fun mainPage(model: Model): List<Room> {
        return try {
            val rooms = roomService.getAllRooms()
            model.addAttribute("rooms", rooms)
            logger.info("방 목록 조회 성공: $rooms")
            rooms
        } catch (e: Exception) {
            logger.error("방 목록 조회 실패", e)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "방 목록 조회 실패")
        }
    }

    @PostMapping("/room_make")
    fun createRoom(@RequestParam name: String): Room {
        logger.info("방 생성 요청 - 이름: $name")
        return try {
            roomService.createRoom(name)
        } catch (e: IllegalArgumentException) {
            logger.error("방 생성 실패 - 중복된 이름: $name", e)
            throw ResponseStatusException(HttpStatus.CONFLICT, e.message)
        } catch (e: Exception) {
            logger.error("방 생성 실패 - 예기치 않은 오류: $name", e)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "방 생성 실패")
        }
    }

    @GetMapping("/chatss/{name}")//중복 경로값
    fun getChatRoom(@PathVariable name: String): Room {
        return try {
            val room = roomService.getRoomByName(name) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "방을 찾을 수 없습니다")
            logger.info("방 조회 성공: $room")
            room
        } catch (e: Exception) {
            logger.error("방 조회 실패: $name", e)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "방 조회 실패")
        }
    }

    @GetMapping("/room_list/{roomName}/users")
    fun getUserList(@PathVariable roomName: String): List<String> {
        return try {
            val users = roomService.getUsersInRoom(roomName)
            logger.info("유저 목록 조회 성공: $users")
            users
        } catch (e: IllegalArgumentException) {
            logger.error("유저 목록 조회 실패: $roomName", e)
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "유저 목록 조회 실패")
        } catch (e: Exception) {
            logger.error("유저 목록 조회 실패: $roomName", e)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "유저 목록 조회 실패")
        }
    }

    @GetMapping("/room_list/{roomName}/details")
    fun getRoomWithUsers(@PathVariable roomName: String): RoomWithUsers {
        return try {
            val roomWithUsers = roomService.getRoomWithUsers(roomName)
            logger.info("방 및 유저 목록 조회 성공: $roomWithUsers")
            roomWithUsers
        } catch (e: IllegalArgumentException) {
            logger.error("방 및 유저 목록 조회 실패: $roomName", e)
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "방 및 유저 목록 조회 실패")
        } catch (e: Exception) {
            logger.error("방 및 유저 목록 조회 실패: $roomName", e)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "방 및 유저 목록 조회 실패")
        }
    }
}
