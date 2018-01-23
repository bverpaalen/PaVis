package DataPresentation;

import DataProcessing.KmerProcessing;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/Kmers")
public class KmersRest {
    KmerProcessing KP = new KmerProcessing();

    /**
     *
     * @queryGene specfic gene name
     * @queryGenome genome ids requested
     * @return k-mers from specfic gene from different genomes
     * testurl http://localhost:8080/rest/Kmers/Gene?gene=matK&genome=1&genome=2&genome=3&genome=4&genome=5
     */
    @GET
    @Path("/Gene")
    @Produces(MediaType.APPLICATION_JSON)
    public Response kmers(@Context UriInfo info){
        String output;
        String geneName;
        List<String> genome;

        geneName = info.getQueryParameters().get("gene").get(0);
        genome = info.getQueryParameters().get("genome");

        output = KP.RequestKmers(geneName,genome);
        System.out.println(geneName);

        return Response.status(200).entity(output).build();
    }
}
