package DataPresentation;

import DataProcessing.GeneProcessing;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;


@Path("/Gene")
public class GeneRest {
    GeneProcessing GP = new GeneProcessing();

    // Test Url
    // http://localhost:8080/rest/Gene/NameSpecific?geneName=trnH&genomeId=1

    /**
     * @queryGeneName name of gene
     * @queryGenomeId id of genome of chosen gene
     * @return gene information
     * testUrl http://localhost:8080/rest/Gene/NameSpecific?geneName=psbA&genomeId=1
     */
    @GET
    @Path("/NameSpecific")
    @Produces(MediaType.APPLICATION_JSON)
    public Response gene(@Context UriInfo info){
        String geneName;
        String genomeId;
        String output;

        geneName = info.getQueryParameters().getFirst("geneName");
        genomeId = info.getQueryParameters().getFirst("genomeId");

        output = GP.RequestGene(geneName,genomeId);

        return Response.status(200).entity(output).build();
    }


    /**
     *
     * @queryGenome
     * @return Response with all genes from chosen pangenome
     * testUrl http://localhost:8080/rest/Gene/Pangenome?genome=1&genome=2&genome=3&genome=4&genome=5
     **/
    @GET
    @Path("/Pangenome")
    @Produces(MediaType.APPLICATION_JSON)
    public Response pangenome(@Context UriInfo info){
        List<String> genomeNumbers;

        genomeNumbers = info.getQueryParameters().get("genome");

        String output = GP.PangenomeGeneRequest(genomeNumbers);

        return Response.status(200).entity(output).build();
    }
}
