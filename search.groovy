@Grapes(
    @Grab(group='org.apache.commons', module='commons-lang3', version='3.0')
)

import java.util.regex.Matcher
import java.util.regex.Pattern
import org.apache.commons.lang3.RandomStringUtils

def path = '/Users/zach/workspace/search-test/sample_text'
def files = ['hitchhikers.txt', 'french_armed_forces.txt', 'warp_drive.txt']

def loadFile(file) {
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

Map loadHashSearch(file) {

    Map<String, Integer> map = new HashMap<String, Integer>()
            
    Scanner sc = new Scanner(new File(file))
    while(sc.hasNext()) {
        def word = sc.next()
        if(map.containsKey(word)) {
            def temp = map.get(word) + 1
            map.put(word,temp)
        } else {
            map.put(word, 1)
        }              
    }
    return map
}            
    
 
def start = System.nanoTime()
files.each { file ->
    def text = loadFile("${path}/${file}")
    def hits = 0
    def misses = 0
    (1..1000).each {
        pattern = RandomStringUtils.random(3, "aeiourstwdhmn".getChars())    
        count = exhaustiveSearch(text,pattern)
        if(count > 0) hits += count
        else misses++
    }        
    println "${file}: ${hits} hits, ${misses} misses"
}
def elapsedTime = System.nanoTime() - start
println "EXHAUSTIVE SEARCH TOOK: ${(double)elapsedTime / 1000000.0} milliseconds"



start = System.nanoTime()
files.each { file ->
    def text = loadFile("${path}/${file}")
    def hits = 0
    def misses = 0
    (1..1000).each {
        pattern = RandomStringUtils.random(3, "aeiourstwdhmn".getChars())    
        count = regexSearch(text,pattern)
        if(count > 0) hits += count
        else misses++
    }        
    println "${file}: ${hits} hits, ${misses} misses"
}
elapsedTime = System.nanoTime() - start
println "REGEX SEARCH TOOK: ${(double)elapsedTime / 1000000.0} milliseconds"



Map<String, Map<String, Integer>> hashes = [:]
files.each {
    hashes.put(it, loadHashSearch("${path}/${it}"))
}

start = System.nanoTime()
hashes.each {file, map ->
    def hits = 0
    def misses = 0
    (1..1000).each {
        pattern = RandomStringUtils.random(2, "aeiourstwdhmn".getChars())    
        count = map.get(pattern)
        if(count > 0) hits += count
        else misses++
    }        
    println "${file}: ${hits} hits, ${misses} misses"
    
}
elapsedTime = System.nanoTime() - start
println "HASH SEARCH TOOK: ${(double)elapsedTime / 1000000.0} milliseconds"





