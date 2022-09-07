/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.evoluti.evoluti.unzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JOptionPane;

/**
 *
 * @author rodri
 */
public class UnZip {
    
    public static String PASTA_LOCAL_APP = System.getProperty("user.dir") + File.separator;
    
    public static void main(String... args) {
        try {
            List<String> params = Arrays.asList(args);
            
            boolean isAjuda = params.stream()
                    .filter(p -> p.equals("-a") || p.equals("--ajuda") || p.equals("-h") || p.equals("--help"))
                    .findFirst().isPresent();
            if (isAjuda) {
                StringBuilder ajuda = new StringBuilder();
                ajuda.append("Aplicativo UnZip.jar\n");
                ajuda.append("Abaixo segue uma lista com os possíveis parametros para serem utilizados:\n\n");
                ajuda.append("-a|--ajuda    Lista parametros para ajuda.\n");
                ajuda.append("-r            Se existir esse parametro ele deleta o arquivo depois de descompactado.\n");
                ajuda.append("-zip=         Informar aonde está o arquivo para ser descompactado ex.: '-zip=\"C:\\MinhaPasta\\MeuArquivo.zip\"\n");
                ajuda.append("-saida=       Informar aonde deseja descompactar o arquivo ex.: '-saida=\"C:\\MinhaPasta\\NestaPasta\\\" - Caso não informe será descompactado no local.\n");
                System.out.println(ajuda.toString());
                return;
            }
            
            Optional<String> optZip = params.stream().filter(p -> p.startsWith("-zip=")).findFirst();
            if (!optZip.isPresent()) {
                throw new Exception("Parametro '-zip=' não encontrado!");
            }
            
            Path arquivoZip = Paths.get(optZip.get().replace("-zip=", ""));
            if (!Files.exists(arquivoZip)) {
                throw new Exception("Arquivo informado no parametro '-zip=' não encontrado!");
            }
            
            Optional<String> optPastaSaida = params.stream().filter(p -> p.startsWith("-saida=")).findFirst();
            Path pastaSaida = Paths.get(optPastaSaida.isPresent() ? optPastaSaida.get().replace("-saida=", "") : PASTA_LOCAL_APP);
            if (!Files.isDirectory(pastaSaida)) {
                throw new Exception("Pasta de saida informado no parametro '-saida=' não é válida!");
            }
            
            if (!Files.exists(arquivoZip)) {
                Files.createDirectories(arquivoZip);
            }
            
            unZipFolder(arquivoZip, pastaSaida);
            
            boolean isRemoverZip = params.stream()
                    .filter(p -> p.equals("-r"))
                    .findFirst().isPresent();
            
            if (isRemoverZip) {
                Files.deleteIfExists(arquivoZip);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    public static void unZipFolder(Path source, Path target) throws IOException {
        
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                boolean isDirectory = false;
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }
                Path newPath = zipSlipProtect(zipEntry, target);
                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {
                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
        
    }

    // protect zip slip attack
    public static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir)
            throws IOException {
        Path targetDirResolved = targetDir.resolve(zipEntry.getName());
        
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }
        
        return normalizePath;
    }
}
