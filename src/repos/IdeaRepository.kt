
import com.minnullin.models.*
import com.tribuna.models.IdeaDto
import io.ktor.http.HttpStatusCode

interface IdeaRepository{
    suspend fun getAll():List<IdeaDto>
    suspend fun changeIdeaCounter(model:CounterChangeDto,login:String): IdeaDto
    suspend fun addIdea(idea: IdeaDto): HttpStatusCode
    suspend fun deleteById(id:Int,authorName:String):HttpStatusCode
    suspend fun getById(id:Int): IdeaDto
}