package DataPresentation;

import DataProcessing.PangenomeProcessing;
import com.sun.jersey.spi.resource.Singleton;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Path("/Data")
@Singleton
public class PangenomeRest extends HttpServlet {
    PangenomeProcessing pangenomeProcessing = new PangenomeProcessing();

    /**
     *
     * @queryGenome requested genomes
     * @return all kmers from selected genome
     * @throws IOException
     * @throws NoSuchMethodError
     */
    @GET
    @Path("/Pangenomes")
    @Produces(MediaType.APPLICAzTION_JSON)
    public Response Pangenomes(@Context UriInfo info) throws IOException, NoSuchMethodError{
        List<String> genomeNames = new ArrayList();

        // Testing url
        // http://localhost:8080/rest/Pangenome/test?genome=a1_1&genome=a2_1
        genomeNames = info.getQueryParameters().get("genome");

        String output = pangenomeProcessing.testGenome(genomeNames);

        return Response.status(200).entity(output).build();
    }

}

