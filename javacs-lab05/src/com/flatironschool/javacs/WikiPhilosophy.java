package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {
    
    final static WikiFetcher wf = new WikiFetcher();
    
    /**
     * Tests a conjecture about Wikipedia and Philosophy.
     * 
     * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
     * 
     * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        List<String> history = new ArrayList<String>(); //List of URLs previously visited

        String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
        history.add(url); //Adds the url we just visited to the history log
        

        do {
            System.out.println("=== current url? ==> " + url);
            Elements paragraphs = wf.fetchWikipedia(url); //Fetches and parses a URL string, returning a list of paragraph elements
            Element firstPara = paragraphs.get(0); //Gives us the first paragraph from the Page
            Elements links = firstPara.select("a[href]"); //Gives us a list of nodes with links from the first paragraph
            Element firstLink = links.get(0); // Gives us the first node with a link from the paragraph

            Iterable<Node> iter = new WikiNodeIterable(firstLink);
            for (Node node: iter) {
                if (node instanceof Element) { 
                    String tempUrl = node.absUrl("href"); // Gets the absolute URL of the element's href attribute
                    if (history.indexOf(tempUrl) == -1){ //Rudimentary Check that the link has not been visited before
                        // if it's a unique link [1] and it's not a local reference [2]
                            url = tempUrl;
                            history.add(url);
                            break;
                    }
                }
            }

        } while (false == url.equalsIgnoreCase("https://en.wikipedia.org/wiki/Philosophy"));

        System.out.println("====== PRINTING OUT HISTORY =====");

        for (String entry : history) {
            System.out.println(entry);
        }
    } 

    /*
    * So I can't get any of my print statements to show up in the stdOut file ... so I can't quite figure
    * out what's going wrong. I just wanted to check that this cycles through links without making exceptions
    # for links in parenthesis or in italics, but because I have no real way of checking how my 
    * implementation actually runs right now -- having no tests to check against is pretty difficult
    * sometimes... -- I'd like to just detail how I'd solve this problem hypothetically.

    * Assuming the above code iterates through the list of nodes that are elements and can parse for the
    * attribtues that pertain to links (href) and create the absolute href's needed in order to fetch the page
    * I would handle that the link is a unique link, [1], by comparing it to the entries in my history log; doing a
    * a simple equalsIgnoreCase to handle the ones that are on separate pages; to handle the urls that are 
    * links to the same page [2] I'd probably parse links for the string after the fourth '/' and filter for the '#'
    * character to remove pages that link to the same wikipage but reference a different html element as
    * a starting point

    * To handle links that are in parenthesis I'd have a parenthesis counter that increments when it comes accross
    * the '(' character and decrements at the ')' character; if a link is found at a point in time where the counter
    * is greater than 0, I know the link is in parenthesis and can be ignored

    * To handle italicized links I'd first find a link and then traverse up it's parent until I hit a <i> flag
    * or when I hit the </i> or the root. This way I know that the link I'm looking at is not in italics.

    * Further more, I'd start with the first paragraph and iterate through but if a valid link cannot be found
    * I must run the same set of function calls on the second paragraph (and so forth) so it'd really be better
    * to write this as a searchParagraph function and call it on the nth one ideally -- and then the n+1th after that
    
    * I never hit the error thrown where I iterate to past the end of the iterator... so I don't know just 
    * quite where I'd throw the error...

    */
}
