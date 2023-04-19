package webplus.smartvillage

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Updates
import org.bson.Document
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.web.bind.annotation.*

/**
 * Date：2023/4/19  21:37
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */


data class SoilCrop(val soil: String, val crop: List<String>)

val soilCropList = listOf(
    SoilCrop("沙土", listOf("花生", "甜菜", "玉米")),
    SoilCrop("粘土", listOf("水稻", "小麦", "玉米", "棉花")),
    SoilCrop("壤土", listOf("棉花", "烟草", "蔬菜")),
    SoilCrop("砂壤土", listOf("玉米", "高粱", "甜菜", "花生")),
    SoilCrop("黏壤土", listOf("稻谷", "小麦", "玉米", "甜菜"))
)

@CrossOrigin("*")
@RestController
class SoilCropController {

    @GetMapping("/api/soil/crop")
    fun getSoilAndCrop(): SoilCrop {
        val randomIndex = (0 until soilCropList.size).random()
        return soilCropList[randomIndex]
    }
}
