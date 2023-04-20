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


data class SoilCrop(val soil: String, val crop: List<String>, val description: String)

val soilCropList = listOf(
    SoilCrop(
        "沙土",
        listOf("花生", "甜菜", "玉米"),
        "沙土是一种由大量石英、长石和其他矿物颗粒组成的土壤。它们通常呈现出明显的颗粒感，非常疏松，透气性好，但缺乏有机质和肥力。沙土通常在干旱地区或海滩上发现。"
    ),
    SoilCrop(
        "粘土",
        listOf("小麦", "玉米", "棉花"),
        "粘土是一种由细小的颗粒组成的土壤，主要由黏土矿物质组成。粘土因其形态和化学结构而具有高度的可塑性和黏性，能够在水中吸附并交换离子，这使得粘土成为许多农业和建筑工程中不可或缺的材料。"
    ),
    SoilCrop(
        "壤土",
        listOf("棉花", "烟草", "蔬菜"),
        "壤土是一种混合了不同粒径的土壤，含有足够的有机质和养分，以支持植物生长。因此，壤土是最理想的农业土壤之一，并且广泛应用于农业生产中。"
    ),
    SoilCrop(
        "砂壤土",
        listOf("玉米", "高粱", "甜菜", "花生"),
        "砂壤土是一种介于沙土和壤土之间的土壤类型，通常包含约70％的沙和30％的黏土和有机质。这种土壤通常干燥、排水良好，适合于一些干旱地区的农业。"
    ),
    SoilCrop(
        "黏壤土",
        listOf("稻谷", "小麦", "玉米", "甜菜"),
        "黏壤土是一种黏性很强的土壤类型，含有较高比例的黏土颗粒和有机物质，对植物生长非常有利。但由于其黏性，它们也容易出现水涝问题，因此需要特殊的管理技术来保持适宜的湿度。"
    ),

    SoilCrop(
        "黄棕壤",
        listOf("豆类", "小麦", "玉米", "花生"),
        "黄棕壤是山西省主要的土壤类型之一，它们通常呈现出黄褐色或棕色，含有丰富的有机质和养分。"
    ),
    SoilCrop(
        "山地灰钙土",
        listOf("葡萄", "苹果", "梨子"),
        "山地灰钙土是山西省山区常见的土壤类型，通常呈现出淡灰色或浅黄色。这种土壤排水良好。"
    ),
    SoilCrop(
        "紫色土",
        listOf("茶叶","草药作物"),
        "紫色土在山西省南部比较常见，这种土壤呈现出紫色或暗红色，含有较高的铁和铝含量，但缺乏有机质和肥力。"
    ),
    SoilCrop(
        "丘陵黄绵土",
        listOf("荞麦","大豆","马铃薯"),
        "丘陵黄绵土是山西省中南部常见的土壤类型，通常呈现出黄色，含有丰富的粘粒和养分。这种土壤适合种植荞麦、大豆、马铃薯等作物。"
    ),
    SoilCrop(
        "红壤",
        listOf("茶叶", "桑叶", "黄花菜"),
        "红壤在山西省东南部比较常见，这种土壤呈现出红色，含有丰富的养分和有机质。"
    )

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
