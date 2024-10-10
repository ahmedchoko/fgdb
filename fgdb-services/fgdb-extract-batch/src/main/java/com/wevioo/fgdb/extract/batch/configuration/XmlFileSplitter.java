package com.wevioo.fgdb.extract.batch.configuration;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlFileSplitter {

    public static List<Chunk> splitFile(InputStream xmlInputStream, int chunkSize) {
        List<Chunk> chunks = new ArrayList<>();
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            DepositorHandler handler = new DepositorHandler(chunks, chunkSize);
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(xmlInputStream));
        } catch (Exception e) {
            throw new RuntimeException("Error splitting file", e);
        } finally {
            try {
                xmlInputStream.close();
            } catch (Exception e) {
            }
        }
        return chunks;
    }

    public static class Chunk {
        private final int id;
        private final List<String> elements;

        public Chunk(int id) {
            this.id = id;
            this.elements = new ArrayList<>();
        }

        public void addElement(String element) {
            elements.add(element);
        }

        public List<String> getElements() {
            return elements;
        }

        public int getId() {
            return id;
        }
    }

    private static class DepositorHandler extends DefaultHandler {
        private final List<Chunk> chunks;
        private final int chunkSize;
        private int elementCount = 0;
        private int currentChunkId = 0;
        private Chunk currentChunk;
        private StringBuilder currentElement;
        private boolean insideDepositor = false;
        private boolean insideDepositers = false;
        private boolean insideVuc = false;
        private String vucId = null;


        public DepositorHandler(List<Chunk> chunks, int chunkSize) {
            this.chunks = chunks;
            this.chunkSize = chunkSize;
            this.currentChunk = new Chunk(currentChunkId);
            this.currentElement = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (localName.equals("vuc")) {
                insideVuc = true;
                vucId = attributes.getValue("vuc_id");
                appendVucStart();
            }

            if (insideVuc && localName.equals("depositors")) {
                insideDepositers = true;
            }

            if (insideDepositers && localName.equals("depositor")) {
                insideDepositor = true;
                currentElement.append("<").append(qName);
                for (int i = 0; i < attributes.getLength(); i++) {
                    currentElement.append(" ")
                            .append(attributes.getQName(i))
                            .append("=\"")
                            .append(attributes.getValue(i))
                            .append("\"");
                }
                currentElement.append(">");
            } else if (insideDepositor && !localName.equals("depositor")) {
                currentElement.append("<").append(qName);
                for (int i = 0; i < attributes.getLength(); i++) {
                    currentElement.append(" ")
                            .append(attributes.getQName(i))
                            .append("=\"")
                            .append(attributes.getValue(i))
                            .append("\"");
                }
                currentElement.append(">");
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (insideDepositor) {
                currentElement.append(new String(ch, start, length));
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (insideDepositers && localName.equals("depositor")) {
                currentElement.append("</").append(qName).append(">");
                elementCount++;
                if (elementCount >= chunkSize) {
                    closeAndSaveChunk();
                    elementCount = 0;
                    currentChunkId++;
                    currentChunk = new Chunk(currentChunkId);
                    appendVucStart();
                }
                insideDepositor = false;
            } else if (insideDepositor) {
                currentElement.append("</").append(qName).append(">");
            }

            if (localName.equals("depositors")) {
                insideDepositers = false;
                currentElement.append("</").append(qName).append(">");
            }

            if (localName.equals("vuc")) {
                insideVuc = false;
                currentElement.append("</").append(qName).append(">");
            }
        }

        @Override
        public void endDocument() throws SAXException {
            if (currentElement.length() > 0 && elementCount > 0) {
                closeAndSaveChunk();
            }
        }

        private void closeAndSaveChunk() {
            if (insideDepositers) {
                currentElement.append("</depositors>");
            }
            if (insideVuc) {
                currentElement.append("</vuc>");
            }

            if (currentElement.length() > 0) {
                currentChunk.addElement(currentElement.toString());
                chunks.add(currentChunk);
            }
            currentElement.setLength(0);
        }

        private void appendVucStart() {
            currentElement.append("<vuc");
            if (vucId != null) {
                currentElement.append(" vuc_id=\"").append(vucId).append("\"");
            }
            currentElement.append("><depositors>");
        }
    }
}
