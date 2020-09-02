import com.minnullin.models.*
import com.tribuna.models.IdeaDto
import io.ktor.features.NotFoundException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*

class IdeaRepositoryBasic : IdeaRepository {
    private var id: Int = 3
    private val mutex = Mutex()
    private var idealist = mutableListOf<IdeaDto>(
        IdeaDto(
            0,
            "Mars",
            "I'm chocolate",
            Date(),
            null,
            byteArrayOf()
        ),
        IdeaDto(
            1,
            "KitKat",
            "I'm chocolate",
            Date(),
            null,
            byteArrayOf()
        ),
        IdeaDto(
            2,
            "Milka",
            "I'm chocolate",
            Date(),
            null,
            byteArrayOf()
        ),
        IdeaDto(
            3,
            "Twix",
            "I'm chocolate",
            Date(),
            "https://animego.org/anime/vnuk-mudreca-k945",
            byteArrayOf()
        )
    )

    override suspend fun getAll(): List<IdeaDto> {
        return idealist.toList()
    }

    private suspend fun getAutoIncrementedId(): Int {
        mutex.withLock {
            id++
            return id
        }
    }

    override suspend fun addIdea(idea: IdeaDto): HttpStatusCode {
        val ideaWithId = IdeaDto(
            id = getAutoIncrementedId(),
            authorName = idea.authorName,
            ideaText = idea.ideaText,
            ideaDate = idea.ideaDate,
            link = idea.link,
            ideaDrawable = idea.ideaDrawable
        )
        idealist.add(ideaWithId)
        return HttpStatusCode.Accepted
    }

    override suspend fun deleteById(id: Int, authorName: String): HttpStatusCode {
        mutex.withLock {
            val findPost = idealist.find {
                it.id == id
            }
            if (findPost != null) {
                if (findPost.authorName != authorName) {
                    return HttpStatusCode.Forbidden
                }
                for (i in 0 until idealist.size) {
                    if (id == idealist[i].id) {
                        idealist.removeAt(i)
                        return HttpStatusCode.Accepted
                    }
                }
            }
            return HttpStatusCode.NotFound
        }
    }

    override suspend fun getById(id: Int): IdeaDto {
        mutex.withLock {
            var ideaToReturn: IdeaDto? = null
            for (i in 0 until idealist.size) {
                if (id == idealist[i].id) {
                    ideaToReturn = idealist[i]
                }
            }
            if (ideaToReturn == null) {
                throw NotFoundException()
            }
            return ideaToReturn
        }
    }

    override suspend fun changeIdeaCounter(model: CounterChangeDto, login: String): IdeaDto {
        mutex.withLock {
            return getById(model.id)
        }
    }
}