import java.util.Scanner
import kotlin.system.exitProcess

val scanner = Scanner(System.`in`)

class App<T : NamedItem>(
    private val items: MutableList<T>,
    private val itemType: String,
    private val exitMenu: App<*>? = null
) {

    fun menu() {
        while (true) {
            println("Выберите необходимый пункт:")
            println("0. Создать ${getItemText()}")
            printItems()
            println("${items.size + 1}. ${if (exitMenu == null) "Выход" else "Вернуться на предыдущий экран"}")

            val choice = scanner.nextLine()
            choose(choice)
        }
    }

    private fun printItems() {
        items.forEachIndexed { index, item -> println("${index + 1}. ${item.name}") }
    }

    private fun choose(choice: String) {
        when (choice) {
            "0" -> create()
            "${items.size + 1}" -> if (exitMenu == null) exit() else exitMenu.menu()
            else -> {
                val index = choice.toIntOrNull()
                if (index != null && index in 1..items.size) {
                    open(index - 1)
                } else {
                    println("Неверный выбор. Пожалуйста, выберите снова.")
                }
            }
        }
    }

    private fun create() {
        while (true) {
            println("Введите имя ${getCreateItemText()} (или 'exit' для выхода)")
            val name = scanner.nextLine().trim()

            if (name.equals("exit", ignoreCase = true)) {
                break
            }

            if (name.isEmpty()) {
                println("${getItemText()} без имени нельзя создать")
                continue
            }

            if (itemType == "заметку") {
                println("Введите текст заметки:")
                val text = scanner.nextLine().trim()
                if (text.isEmpty()) {
                    println("${getItemText()} без текста нельзя создать")
                    continue
                }
                items.add(createItem(name, text))
            } else {
                items.add(createItem(name))
            }

            println("${getNewItemText()} $name создан(а)")
            break
        }
    }

    private fun open(index: Int) {
        val selectedItem = items[index]
        println("Открыт(а) ${getNewItemText()}: ${selectedItem.name}")

        if (selectedItem is Archive) {
            val noteManager = App(selectedItem.notes, "заметку", this)
            noteManager.menu()
        } else if (selectedItem is Note) {
            while (true) {
                println("Заметка: ${selectedItem.name}")
                println("Текст: ${selectedItem.text}")
                println("Введите 'exit' для возврата к списку заметок")

                val input = scanner.nextLine().trim()
                if (input.equals("exit", ignoreCase = true)) {
                    break
                }
            }
        }
    }

    private fun exit() {
        println("Выход из программы.")
        exitProcess(0)
    }

    private fun createItem(name: String, text: String = ""): T {
        return when (itemType) {
            "архив" -> Archive(name) as T
            "заметку" -> Note(name, text) as T
            else -> throw IllegalArgumentException("Неизвестный тип элемента")
        }
    }

    private fun getCreateItemText(): String {
        return when (itemType) {
            "архив" -> "архива"
            "заметку" -> "заметки"
            else -> throw IllegalArgumentException("Неизвестный тип элемента")
        }
    }

    private fun getNewItemText(): String {
        return when (itemType) {
            "архив" -> "архив"
            "заметку" -> "заметка"
            else -> throw IllegalArgumentException("Неизвестный тип элемента")
        }
    }

    private fun getItemText(): String {
        return when (itemType) {
            "архив" -> "архив"
            "заметку" -> "заметку"
            else -> throw IllegalArgumentException("Неизвестный тип элемента")
        }
    }
}

