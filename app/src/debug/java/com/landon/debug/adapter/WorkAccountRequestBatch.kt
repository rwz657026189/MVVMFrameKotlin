package com.landon.debug.adapter

/**
 * @Author Ren Wenzhang
 * @Date 2022/7/18/018 17:31
 * @Description
 */
class WorkAccountRequestBatch {

    fun recordWork() {
        val curl =
            "curl -X POST -H \"Authorization:Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vZ3R4dC52cnRiYnMuY29tL2FwaS91c2VyL2xvZ2luUGhvbmUiLCJpYXQiOjE2NTgxMTMwNDQsImV4cCI6MTY2MDcwNTA0NCwibmJmIjoxNjU4MTEzMDQ0LCJqdGkiOiI0MFpDYlZVNHh3dWttUWFmIiwic3ViIjoiNDM4IiwicHJ2IjoiMTVkMmU4MzBlMzUzNmIxNTVlMDg1ZTc3YzM2NmEzOTE0NTBlMGQ0YSIsInJvbGUiOiJ1c2VyIn0.jh9WBqMzBwwE66TxTQAiB1vWBsHsOcK2rhD8VlOIqE8\" -H \"Source:android\" -H \"X-Requested-With:XMLHttpRequest\" -H \"App-Version:1.4.0\" -H \"Device-Name:HONOR,KOZ-AL00\" -H \"Content-Type:application/json;charset=utf-8\" -d '{\"dept_id\":\"2594\",\"staff_id\":[\"1188\"],\"date\":[\"2022-07-18\"],\"remark\":\"\",\"image\":[],\"note_day_type\":\"1\",\"note_work_type\":\"1\",\"note_work\":\"1\",\"work_extra_hours\":\"0\",\"note_work_4\":\"0,0\",\"status_check\":\"{\\\"content\\\":[{\\\"id\\\":\\\"1\\\",\\\"type\\\":\\\"work\\\",\\\"value\\\":\\\"1\\\"},{\\\"value\\\":\\\"0\\\"},{\\\"value\\\":\\\"0\\\"}],\\\"extraTime\\\":\\\"0\\\",\\\"noteType\\\":\\\"1\\\"}\"}' \"http://gtxt.vrtbbs.com/api/note/work/addWork\""

        executor(curl)
    }

    private fun replaceValue(curl: String, dest: String, value: String): String {
        curl.indexOf("\"$dest\":")
        return curl
    }

    private fun executor(curl: String) {

    }

}