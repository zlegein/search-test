import java.util.regex.Matcher
import java.util.regex.Pattern

def path = './sample_text'
def files = ['hitchhikers.txt', 'french_armed_forces.txt', 'warp_drive.txt']

def loadFile(file) {
    println file
    return new File(file).text
}

def exhaustiveSearch (text, pattern) {    
    def textLength = text.length()
    def patternLength = pattern.length()
    def count = 0
    for (i = 0; i < textLength - patternLength; i++) {
        def j = 0 
        while (j < patternLength && text[i+j] == pattern[j]) {
            j++
        }
        if(j == patternLength) count++
    }
    count
}

def regexSearch(text, pattern) {
    Pattern p = Pattern.compile(pattern)
    Matcher m = p.matcher(text)
    def count = 0
    while(m.find()) {
        count++
    }
    count
}

Map loadHashes() {
    Map hashes = [:]
    files.each {
        hashes.put(it, loadFile("${path}/${it}"))
    }
}

//Map loadHashSearch(file) {
//    Map wordCountMap = [:]
//    Pattern p = Pattern.compile('\\w*')
//    Matcher m = p.matcher("this is a this test is this nice")
//    while(m.find()) {
//        String word = m.group()        
//        if(word) {                                  
//            if(wordCountMap["$word"]) {
//                Integer count = wordCountMap["$word"]
//                println "what is count:  $count"
//                wordCountMap["$word"] = count++
//                println "setting word: ${word} to count ${count}"
//            } else {
//                count = 1
//                wordCountMap["$word"] = new Integer(count)
////                println "word ${word} is a new entry setting 1"
//            }
//            println "$word : $count"
//        }
//    }
//    return wordCountMap
//            
//    Scanner sc = new Scanner(new File(file))
//    while(sc.hasNextLine()) {
//        def Scanner s = new Scanner(sc.nextLine())
//        while(s.hasNext()) {
//            def word = s.next()
//            if(word) {
//                def count = map[word]
//                if(count) {
//                    map[word] = count++
//                    println "setting word: ${word} to count ${count}"
//                } else {
//                    map[word] = 1
//                    println "word ${word} is a new entry setting 1"
//                }               
//            }
//        }
//    }
//    return map
//}            
    
    
    

def start = System.nanoTime()
files.each {
    def text = loadFile("${path}/${it}")
    count = exhaustiveSearch(text,'a')
    println "${it}: ${count}"
}
def elapsedTime = System.nanoTime() - start
println "EXHAUSTIVE SEARCH TOOK: ${(double)elapsedTime / 1000000.0} milliseconds"



start = System.nanoTime()
files.each {
    def text = loadFile("${path}/${it}")
    count = regexSearch(text,'a')
    println "${it}: ${count}"
}
elapsedTime = System.nanoTime() - start
println "REGEX SEARCH TOOK: ${(double)elapsedTime / 1000000.0} milliseconds"



Map hashes = [:]
files.each {
    hashes.put(it, loadFile("${path}/${it}"))
}



start = System.nanoTime()
hashes.each {k, v ->
    count = regexSearch(v,'a')
    println "${k}: ${count}"
    
}
elapsedTime = System.nanoTime() - start
println "HASH SEARCH TOOK: ${(double)elapsedTime / 1000000.0} milliseconds"






